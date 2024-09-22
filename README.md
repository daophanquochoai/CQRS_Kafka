

# CQRS with Kafka

## Tác giả
**Đào Phan Quốc Hoài**

## Mục lục
1. [Giới thiệu về CQRS](#giới-thiệu-về-cqrs)
2. [Tích hợp Kafka với CQRS](#tích-hợp-kafka-với-cqrs)
3. [Cách triển khai CQRS với Kafka](#cách-triển-khai-cqrs-với-kafka)
4. [Lợi ích của CQRS kết hợp với Kafka](#lợi-ích-của-cqrs-kết-hợp-với-kafka)
5. [Kết luận](#kết-luận)

---

## Giới thiệu về CQRS

**CQRS** (Command Query Responsibility Segregation) là một mẫu kiến trúc phần mềm tách biệt hai phần chính:
- **Command**: Thao tác ghi, cập nhật dữ liệu.
- **Query**: Truy vấn dữ liệu.

Mẫu thiết kế CQRS giúp tối ưu hóa hiệu suất của hệ thống, đặc biệt trong các hệ thống có quy mô lớn. Nó cho phép tách biệt cách thức xử lý dữ liệu khi ghi và đọc, từ đó dễ dàng mở rộng từng phần một cách độc lập.

---

## Tích hợp Kafka với CQRS

**Kafka** là một nền tảng phân phối tin nhắn theo thời gian thực, lý tưởng cho các hệ thống yêu cầu xử lý và truyền tải dữ liệu lớn với độ trễ thấp. Trong bối cảnh CQRS, Kafka thường được sử dụng để truyền tải các **sự kiện (events)** từ phần Command (ghi dữ liệu) đến phần Query (đọc dữ liệu).

### Vai trò của Kafka trong CQRS:
- **Event Sourcing**: Mỗi khi một hành động ghi dữ liệu (Command) được thực hiện, một sự kiện tương ứng sẽ được xuất ra Kafka. Các dịch vụ khác có thể tiêu thụ sự kiện này để cập nhật trạng thái của mình.
- **Tối ưu hóa xử lý bất đồng bộ**: Kafka giúp các thành phần trong hệ thống giao tiếp với nhau mà không cần chờ đợi lẫn nhau, tăng hiệu suất và khả năng mở rộng.

---

## Cách triển khai CQRS với Kafka

### 1. Cấu trúc hệ thống

- **Command Service**: Thực hiện các thao tác ghi dữ liệu. Mỗi lần có một thay đổi (ví dụ: tạo, cập nhật, xóa), sự kiện tương ứng sẽ được gửi lên Kafka.
- **Query Service**: Lắng nghe các sự kiện từ Kafka để cập nhật mô hình dữ liệu chỉ dùng cho đọc (Read Model). Query Service chỉ thực hiện các thao tác truy vấn, không thay đổi trạng thái hệ thống.

### 2. Triển khai cơ bản

#### a. Command Service
- Xử lý yêu cầu ghi dữ liệu.
- Sau khi ghi thành công, gửi một sự kiện tương ứng lên Kafka.

Ví dụ code với Spring Boot Kafka Producer:

```java
 public Product createProduct( Product product){
        Product productSave = productRepository.save(product);
        ProductEvent event = new ProductEvent("CreateProduct", productSave.getId(),
        productSave.getName(),
        productSave.getDescription(),
        productSave.getPrice());
        kafkaTemplate.send("product-event-topic", event);
        return productSave;
        }
public Product updateProduct( Long id, Product product){
        Product existProduct = productRepository.findById(id).get();
        existProduct.setName(product.getName());
        existProduct.setDescription(product.getDescription());
        existProduct.setPrice(product.getPrice());
        Product productUpdated = productRepository.save(existProduct);
        ProductEvent event = new ProductEvent("UpdateProduct", productUpdated.getId(),
        productUpdated.getName(),
        productUpdated.getDescription(),
        productUpdated.getPrice());
        kafkaTemplate.send("product-event-topic", event);
        return productUpdated;
        }
```

#### b. Query Service
- Tiêu thụ sự kiện từ Kafka và cập nhật Read Model.

Ví dụ code với Spring Boot Kafka Consumer:

```java
    @KafkaListener(topics = "product-event-topic", groupId = "doctorhoai")
public void processProductEvents(ProductEvent productEvent ){
        Product product = new Product(productEvent.getId(),
        productEvent.getName(),
        productEvent.getDescription(),
        productEvent.getPrice()
        );
        if( productEvent.getEventType().equals("CreateProduct") ){
        productRepository.save(product);
        }
        else if( productEvent.getEventType().equals("UpdateProduct") ){
        Product existingProduct = productRepository.findById(product.getId()).get();
        existingProduct.setPrice(product.getPrice());
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        }
        }
```

### 3. Cơ chế xử lý dữ liệu
- **Event Sourcing**: Lưu trữ toàn bộ các sự kiện xảy ra trong hệ thống. Các dịch vụ đọc sẽ tiêu thụ và xử lý các sự kiện này để cập nhật mô hình dữ liệu.

---

## Lợi ích của CQRS kết hợp với Kafka

1. **Khả năng mở rộng cao**: CQRS kết hợp với Kafka giúp hệ thống xử lý dữ liệu một cách bất đồng bộ, tăng khả năng mở rộng và hiệu năng tổng thể.
2. **Tách biệt giữa ghi và đọc**: Việc sử dụng Kafka để truyền tải sự kiện giữa Command và Query cho phép tối ưu hóa các thao tác ghi và đọc độc lập.
3. **Khả năng phục hồi và chịu lỗi**: Kafka lưu trữ các sự kiện, cho phép khôi phục lại trạng thái hệ thống bằng cách tái phát lại các sự kiện nếu cần.
4. **Giảm thiểu độ trễ**: Nhờ khả năng xử lý bất đồng bộ, hệ thống có thể xử lý lượng lớn yêu cầu mà không làm giảm hiệu năng.

---

## Kết luận

Việc sử dụng **CQRS** kết hợp với **Kafka** là một giải pháp mạnh mẽ trong việc xây dựng các hệ thống phân tán có yêu cầu cao về khả năng mở rộng và hiệu suất. Kafka giúp tăng tính bất đồng bộ, trong khi CQRS tách biệt trách nhiệm giữa ghi và đọc, mang lại một hệ thống linh hoạt và mạnh mẽ hơn.

Nếu bạn có thắc mắc hoặc cần thêm thông tin chi tiết, vui lòng liên hệ với **Đào Phan Quốc Hoài** để được hỗ trợ.

--- 

**Tài liệu tham khảo**:
- [CQRS Pattern](https://martinfowler.com/bliki/CQRS.html)
- [Kafka Documentation](https://kafka.apache.org/documentation/)

---