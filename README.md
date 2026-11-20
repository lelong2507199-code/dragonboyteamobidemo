# Dragon Boy Rebuild — Android + Java API + MariaDB

Dự án game 2D độc lập, dùng asset tự tạo theo phong cách RPG pixel cổ điển; không chứa mã nguồn hoặc tài nguyên độc quyền TeaMobi.

## Đã tích hợp trong bản test
- Client HTML5 Canvas đóng gói trong Android WebView; atlas PNG được nhúng trong APK.
- Điều khiển trái/phải, nhảy, đánh thường, kỹ năng tiêu hao MP, quái có HP, hồi phục.
- API Java 21 + MariaDB: đăng ký/đăng nhập, token phiên 30 ngày, tạo/tải nhân vật, combat xử lý và lưu EXP/zeni/kills/level ở server.
- Schema mới và script nâng cấp database v1.

> Đây là vertical-slice để kiểm thử luồng client-server, chưa phải game hoàn chỉnh: chưa có multiplayer realtime, nhiều map, nhiệm vụ, trang bị/shop và hệ thống quái dữ liệu phong phú. Atlas là ảnh sprite tổng hợp; một số sprite đang được lấy từ vùng cắt trong atlas, chưa phải animation sheet được làm sạch từng frame.

## 1. Database trên Termux
```bash
pkg update -y && pkg install openjdk-21 mariadb -y
mariadb-install-db   # chỉ chạy lần đầu nếu chưa khởi tạo
mysqld_safe --datadir=$PREFIX/var/lib/mysql &
mariadb -u root < database/schema.sql
```
Nếu đã cài schema bản cũ, chạy:
```bash
mariadb -u root < database/migrate_from_v1.sql
```

## 2. Chạy server
```bash
cd server
chmod +x gradlew
DB_URL='jdbc:mariadb://127.0.0.1:3306/dragonboy' DB_USER=root DB_PASS='' PORT=8080 ./gradlew run
```
Kiểm tra `http://127.0.0.1:8080/health` phải trả JSON có `"ok":true`.

## 3. Chạy client / APK
Màn hình đăng nhập có ô API URL. Trên điện thoại thật nhập IP LAN của thiết bị chạy Termux, ví dụ `http://192.168.1.10:8080`; không nhập localhost nếu server nằm trên thiết bị khác. Hai thiết bị cần cùng mạng Wi-Fi. Emulator thường dùng `http://10.0.2.2:8080`.

## 4. Build APK
Push toàn bộ repo lên GitHub. Workflow `.github/workflows/android-apk.yml` tự chạy `assembleDebug`; vào Actions → workflow mới nhất → Artifacts → `dragonboy-debug-apk` để tải APK test. Có thể mở thư mục `android/` bằng Android Studio và chạy Build APK(s).

## API v2
- `GET /health`
- `POST /api/register` hoặc `/api/login` → trả `token`
- Gửi `Authorization: Bearer <token>` cho các API sau
- `POST /api/logout`
- `POST /api/character/create` `{ "name":"Warrior", "classId":0 }`
- `GET /api/character`
- `POST /api/game/attack` `{ "skill":false }` hoặc `{ "skill":true }`
- `POST /api/game/heal` `{}`

## Bảo mật trước phát hành
Bản test dùng HTTP cleartext và CORS mở để thuận tiện LAN. Trước phát hành công khai cần HTTPS, rate-limit theo IP/tài khoản, quản lý refresh/revoke token, xác thực dữ liệu di chuyển và kiểm thử tải. Không dùng cấu hình test này làm server production.
