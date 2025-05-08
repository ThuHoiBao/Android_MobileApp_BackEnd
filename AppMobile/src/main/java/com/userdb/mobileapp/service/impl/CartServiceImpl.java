package com.userdb.mobileapp.service.impl;

import com.userdb.mobileapp.dto.requestDTO.AddProductToCartRequestDTO;
import com.userdb.mobileapp.dto.requestDTO.CartItemUpdateRequestDTO;
import com.userdb.mobileapp.dto.responseDTO.CartItemDTO;
import com.userdb.mobileapp.dto.responseDTO.CartItemOutOfStock;
import com.userdb.mobileapp.entity.*;
import com.userdb.mobileapp.exception.DataNotFoundException;
import com.userdb.mobileapp.repository.CartItemRepository;
import com.userdb.mobileapp.repository.CartRepository;
import com.userdb.mobileapp.repository.ProductRepository;
import com.userdb.mobileapp.repository.UserRepository;
import com.userdb.mobileapp.service.ICart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICart {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public Cart addProductToCart(long userId, AddProductToCartRequestDTO request) throws DataNotFoundException {

        Product product = productRepository
                .findTopByProductNameAndColor(request.getProductName(), request.getColor())
                .orElseThrow(() -> new DataNotFoundException("Product is not found"));

        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(userRepository.findById(userId).orElseThrow());
                    return cartRepository.save(newCart);
                });

        CartItem existingItem = cart.getCardItems().stream()
                .filter(item -> item.getProduct().getProductName().equals(product.getProductName()) &&
                        item.getProduct().getColor().equals(product.getColor()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
        }

        return cart;
    }

    @Override
    public List<CartItem> updateProductInCart(List<CartItemUpdateRequestDTO> updates, long userId) throws DataNotFoundException {
            List<CartItem> cartItemList = new ArrayList<>();
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new DataNotFoundException("Cart not found"));
        for (CartItemUpdateRequestDTO update : updates) {
            Product product = productRepository
                    .findTopByProductNameAndColor(update.getProductName(), update.getColor())
                    .orElseThrow(() -> new DataNotFoundException("Product is not found"));

            CartItem existingItem = cart.getCardItems().stream()
                    .filter(item -> item.getProduct().getProductName().equals(product.getProductName()) &&
                            item.getProduct().getColor().equals(product.getColor()))
                    .findFirst()
                    .orElse(null);
            if (existingItem != null) {
                cartItemList.add(existingItem);
                if(update.getNewQuantity() <= 0){
                    cartItemRepository.delete(existingItem);
                } else {
                    existingItem.setQuantity(update.getNewQuantity());
                    cartItemRepository.save(existingItem);
                }
                } else {
                throw new DataNotFoundException("Product is not found in the cart");
            }
        }
        return cartItemList;
    }

    @Override
    public List<CartItemDTO> getCartItemsByUserId(long userId) throws DataNotFoundException {
        // Lấy Cart của người dùng từ userId
        Cart cart = cartRepository.findByUser_Id(userId).orElseThrow(() -> new DataNotFoundException("Cart is not found by user Id: " + userId));

        if (cart == null) {
            return null;  // Nếu không có cart, trả về null
        }

        // Lấy tất cả CartItem của người dùng
        List<CartItem> cartItems = cart.getCardItems();

        // Chuyển đổi CartItem thành CartItemDTO
        return cartItems.stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            String productImage = product.getImageProducts().isEmpty() ? null : product.getImageProducts().get(0).getImageProduct();

            return new CartItemDTO(
                    cartItem.getCartItemId(),
                    product.getProductName(),
                    product.getColor(),
                    product.getPrice(),
                    cartItem.getQuantity(),
                    productImage
            );
        }).collect(Collectors.toList());
    }

    @Override
    public void removeCartItem(long userId, int cartItemId) throws DataNotFoundException {
        CartItem cartItem = cartItemRepository.findByCartItemIdAndCart_User_Id(cartItemId, userId)
                .orElseThrow(() -> new DataNotFoundException("Cart item not found or does not belong to user"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public List<CartItemOutOfStock> updateCartItem(Long userId) throws DataNotFoundException {
        // Bước 1: Lấy tất cả các CartItem của người dùng
        List<CartItem> cartItems = cartItemRepository.findByCart_User_Id(userId);
        List<CartItemOutOfStock> outOfStockItems = new ArrayList<>();

        // Bước 2: Đếm số lượng sản phẩm trong giỏ hàng dựa trên productName và color
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct(); // Lấy sản phẩm từ CartItem
            if (product != null) { // Kiểm tra nếu sản phẩm còn tồn tại và còn kích hoạt
                // Tìm tất cả các sản phẩm có productName và color trong bảng Product giong nhau va status = true
                List<Product> productsWithSameNameAndColor = productRepository.findByProductNameAndColorAndStatusTrue(
                        product.getProductName(), product.getColor());

                int countMap = productsWithSameNameAndColor.size();
                System.out.println(countMap);

                // Bước 3: Lấy thông tin CartItem từ id và cập nhật số lượng trong giỏ hàng nếu cần
                if(countMap == 0){
                    outOfStockItems.add(new CartItemOutOfStock(cartItem.getCartItemId(), true));
                }
                else if (countMap > 0 && cartItem.getQuantity() > countMap) {
                    cartItem.setQuantity(countMap);  // Cập nhật số lượng mới
                    cartItemRepository.save(cartItem);  // Lưu lại thay đổi
                    outOfStockItems.add(new CartItemOutOfStock(cartItem.getCartItemId(), false));
                }
            }
        }
        return outOfStockItems;
    }
}
