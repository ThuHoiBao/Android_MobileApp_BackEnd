# Ứng Dụng Mua Sắm Điện Thoại - Oneway Mobile

## Giới Thiệu Dự Án
Ứng dụng "Oneway Mobile" được phát triển để cung cấp một nền tảng mua sắm điện thoại di động tiện lợi và hiệu quả, giúp người dùng không cần đến cửa hàng trực tiếp. Ứng dụng cho phép người dùng duyệt các mẫu điện thoại khác nhau, truy cập thông tin chi tiết về sản phẩm, đặt hàng trực tuyến và thanh toán an toàn. Bên cạnh đó, người dùng còn có thể đánh giá sản phẩm và chia sẻ trải nghiệm của mình.

## Công Nghệ Sử Dụng
### Frontend (Ứng Dụng Di Động):
- **Android Studio**: Nền tảng phát triển ứng dụng Android, sử dụng ngôn ngữ Java để xây dựng giao diện người dùng thân thiện và trực quan.
- **Glide**: Thư viện giúp tải và hiển thị hình ảnh sản phẩm nhanh chóng.
- **Picasso**: Thư viện tải và hiển thị hình ảnh.
- **Retrofit**: Thư viện giúp thực hiện các yêu cầu HTTP dễ dàng và xử lý dữ liệu trả về.
- **OkHttp**: Thư viện HTTP client để thực hiện các yêu cầu mạng.
- **OkHttp Logging Interceptor**: Ghi lại các thông tin về các yêu cầu HTTP.
- **JWTDecode**: Thư viện giải mã JWT token.
### Backend (Dịch Vụ API):
- **Spring Boot (Java)**: Framework mạnh mẽ để xây dựng các API RESTful, phục vụ giao tiếp giữa ứng dụng di động và hệ thống backend.
- **Spring Security + JWT**:
     +  Spring Security cung cấp cơ chế bảo mật, đảm bảo an toàn khi xác thực người dùng và phân quyền truy cập vào API.
     +  Sử dụng JSON Web Token (JWT) để xác thực và phân quyền người dùng.
- ** Spring Data JPA **: Spring Data JPA: Quản lý truy vấn và tương tác với cơ sở dữ liệu.                        
- ** ModelMapper **:
    + Hỗ trợ ánh xạ tự động giữa các lớp DTO và Entity.
    + Giúp code ngắn gọn, dễ bảo trì, loại bỏ việc mapping thủ công.
### Cơ Sở Dữ Liệu:
- **MySQL**: Hệ quản trị cơ sở dữ liệu quan hệ lưu trữ thông tin người dùng, sản phẩm, đơn hàng, đánh giá,...

### Lưu Trữ Đám Mây:
- **Google Cloud Storage (GCS)**: Giải pháp lưu trữ đám mây giúp quản lý và lưu trữ hình ảnh sản phẩm và đánh giá an toàn, dễ dàng truy cập.

## Kiến Trúc Hệ Thống
Dự án áp dụng **Kiến trúc 3 lớp** kết hợp với **Mô hình client-server**, giúp tách biệt rõ ràng các tầng xử lý và nâng cao khả năng bảo trì, mở rộng, cũng như tối ưu hóa hiệu suất hệ thống.

## Các Chức Năng Chính
1. **Đăng Ký Tài Khoản, Đăng Nhập, Quên Mật Khẩu**
   - Người dùng có thể đăng ký qua Google hoặc nhập số điện thoại và họ tên.
   - Đổi mật khẩu xác nhận qua email đã đăng kí tài khoản.
   
2. **Quản Lý Giỏ Hàng**
   - Xem và chỉnh sửa số lượng sản phẩm, xóa sản phẩm khỏi giỏ hàng và kiểm tra tình trạng tồn kho.

3. **Đặt Hàng**
   - Người dùng có thể đặt hàng từ giỏ hàng, nhập thông tin giao hàng và xác nhận đơn hàng.

4. **Thanh Toán VNPay**
   - Hỗ trợ thanh toán qua cổng VNPay, tự động xác nhận đơn hàng sau khi thanh toán thành công.

5. **Chatbox**
   - Chatbot tích hợp AI (Gemani) giúp trả lời các câu hỏi tự động và hỗ trợ người dùng về các vấn đề như tình trạng đơn hàng, chính sách đổi trả, v.v.

6. **Quản Lý Hồ Sơ Người Dùng**
   - Người dùng có thể cập nhật thông tin cá nhân như ảnh đại diện, họ tên, ngày sinh, số điện thoại và địa chỉ email.

7. **Xem và Tìm Kiếm Sản Phẩm**
- Tính năng tìm kiếm và duyệt sản phẩm theo danh mục hoặc từ khóa, giúp người dùng dễ dàng lựa chọn sản phẩm phù hợp.

8. **Quản Lý Đơn Hàng**
   - Theo dõi tình trạng đơn hàng từ lúc tạo đơn, thanh toán, vận chuyển đến khi hoàn tất nhận hàng hoặc hủy đơn.

9. **Đánh Giá Sản Phẩm**
   - Sau khi nhận hàng, người dùng có thể đánh giá sản phẩm (1-5 sao) và để lại nhận xét, kèm theo hình ảnh thực tế của sản phẩm.

10. **Xem Chi Tiết Sản Phẩm**
    - Cung cấp thông tin chi tiết về sản phẩm bao gồm hình ảnh, giá, đánh giá từ người dùng khác, v.v.

## Cài Đặt và Hướng Dẫn Sử Dụng
### 1. Cài Đặt Frontend:
   - Cài đặt **Android Studio** và clone repository về máy.
   - Mở dự án trong Android Studio và chạy ứng dụng trên thiết bị/emulator Android.

### 2. Cài Đặt Backend:
   - Cài đặt **Spring Boot** và chạy ứng dụng backend để cung cấp API.
   - Sử dụng cơ sở dữ liệu **MySQL** để lưu trữ dữ liệu người dùng, sản phẩm, đơn hàng, v.v.

### 3. Cấu Hình Cổng Thanh Toán VNPay:
   - Đảm bảo cấu hình cổng thanh toán VNPay trong hệ thống backend.

## Đóng Góp
Nếu bạn muốn đóng góp vào dự án, vui lòng thực hiện các bước sau:
1. Fork repository.
2. Tạo một nhánh mới.
3. Thực hiện các thay đổi và gửi pull request.

## Liên Hệ
- Thành viên 1:
 +  Email: thoai12309@gmail.com
 + GitHub: https://github.com/vuongducthoai
- Thành viên 2:
 + Email: trananhthu270904@gmail.com
 + Github: https://github.com/ThuHoiBao
