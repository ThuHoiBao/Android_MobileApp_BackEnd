package com.userdb.mobileapp.service.impl;

import com.userdb.mobileapp.dto.requestDTO.AddProductToCartRequestDTO;
import com.userdb.mobileapp.dto.requestDTO.CartItemUpdateRequestDTO;
import com.userdb.mobileapp.dto.responseDTO.CartItemDTO;
import com.userdb.mobileapp.dto.responseDTO.CartItemUpdateResultDTO;
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
    public List<CartItemUpdateResultDTO> updateProductInCart(List<CartItemUpdateRequestDTO> updates) throws DataNotFoundException {
        List<CartItemUpdateResultDTO> resultList = new ArrayList<>();
        List<CartItem> itemsToSave = new ArrayList<>();

        for (CartItemUpdateRequestDTO update : updates) {
            Product product = productRepository
                    .findTopByProductNameAndColor(update.getProductName(), update.getColor())
                    .orElseThrow(() -> new DataNotFoundException("Product is not found"));

            CartItem existingItem = cartItemRepository.findById(update.getCartItemId())
                    .orElseThrow(() -> new DataNotFoundException("CartItem is not found"));

            List<Product> productsWithSameNameAndColor = productRepository.findByProductNameAndColorAndStatusTrue(
                    product.getProductName(), product.getColor());
            int availableStock = productsWithSameNameAndColor.size();
            int requestedQuantity  = update.getNewQuantity();
            int quantityToSet = Math.min(requestedQuantity, availableStock);

            CartItemUpdateResultDTO result = new CartItemUpdateResultDTO();

            if (availableStock == 0 || quantityToSet == 0) {
//                cartItemRepository.delete(existingItem);
                result.setCartItemId(existingItem.getCartItemId());
//                result.setUpdatedQuantity(0);
                result.setOutOfStock(true);
                result.setMessage("Hết hàng");
            } else {
                existingItem.setQuantity(quantityToSet);
                itemsToSave.add(existingItem);
                result.setCartItemId(existingItem.getCartItemId());
                result.setUpdatedQuantity(quantityToSet);
                result.setOutOfStock(false);
                if (quantityToSet < requestedQuantity) {
                    result.setMessage("Không đủ hàng, đã cập nhật còn " + quantityToSet);
                } else {
//                    result.setMessage("Cập nhật thành công");
                }
            }
            resultList.add(result);
        }

        if(!itemsToSave.isEmpty()){
            cartItemRepository.saveAll(itemsToSave);
        }
        return resultList;
    }

    @Override
    public List<CartItemDTO> getCartItemsByUserId(long userId) throws DataNotFoundException {
        // Bước 1: Lấy tất cả các CartItem của người dùng
        List<CartItem> cartItems = cartItemRepository.findByCart_User_Id(userId);
        List<CartItemDTO> outOfStockItems = new ArrayList<>();

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
                String productImage =  product.getImageProducts().isEmpty() ? null : "https://storage.googleapis.com/bucket_mobileapp/images/" + product.getImageProducts().get(0).getImageProduct();
                if(countMap == 0){ // truong hop het hang
                    outOfStockItems.add(new CartItemDTO(cartItem.getCartItemId(), product.getProductName(), product.getColor(), product.getPrice(), cartItem.getQuantity(), productImage, true));
                }
                else if (cartItem.getQuantity() > countMap) { // truong hop so luong stock nho hon so luong quantity
                    cartItem.setQuantity(countMap);  // Cập nhật số lượng mới
                    cartItemRepository.save(cartItem);  // Lưu lại thay đổi
                    outOfStockItems.add(new CartItemDTO(cartItem.getCartItemId(), product.getProductName(), product.getColor(), product.getPrice(), cartItem.getQuantity(), productImage, false));
                } else { // truong hop con hang
                    outOfStockItems.add(new CartItemDTO(cartItem.getCartItemId(), product.getProductName(), product.getColor(), product.getPrice(), cartItem.getQuantity(), productImage, false));
                }
            }
        }
        return outOfStockItems;
    }

    @Override
    public void removeCartItem(int cartItemId) throws DataNotFoundException {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new DataNotFoundException("Cart item not found"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public CartItem updateProductCartItem(int cartItemId, CartItemUpdateRequestDTO updates) throws DataNotFoundException {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new DataNotFoundException("CartItem is not found"));
        //Lay so luong hien co trong kho dua vao so luong va mau sac voi trang thai con hang
        List<Product> productsWithSameNameAndColor = productRepository.findByProductNameAndColorAndStatusTrue(
                updates.getProductName(), updates.getColor());

        int count = productsWithSameNameAndColor.size(); // Dem so luong san pham
        if(updates.getNewQuantity() > count){ // So luong can dat lon hon so luong hien co trong kho
            cartItem.setQuantity(count);
        }

        return cartItem;
    }
}
