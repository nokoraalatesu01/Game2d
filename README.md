# Whisper Of The Emerald Forest

`Whisper Of The Emerald Forest` la mot game 2D platformer viet bang Java SE voi `Swing/AWT/Java2D`, khong dung engine ngoai. Nguoi choi vao vai mot hiep si thuc hien hanh trinh tim lai vien ngoc luc bao de khoi phuc su binh yen cho khu rung Emerald Forest.

## Bai lam gom

- Menu mo dau co cac muc `Play`, `Resolution`, `Intro`, `Guide`, `Leaderboard`, `Credits`, `Exit`
- Nhap ten nguoi choi truoc khi bat dau
- 2 man choi chinh va 1 man ket thuc
- Di chuyen tren map tile-based, co va cham tuong, mat dat va vung nuoc
- Ke dich Orc co the tan cong nguoi choi
- Vat pham hoi mau roi ra sau khi ha dich
- Man hinh `Pause`, `Death`, `The End`
- Bo dem thoi gian hoan thanh man choi
- Leaderboard online bang Supabase de luu va hien thi thanh tich
- Ho tro nhieu do phan giai va che do toan man hinh

## Muc tieu gameplay

Nguoi choi dieu khien nhan vat vuot qua cac man choi, tieu diet toan bo ke dich de mo duong sang khu vuc tiep theo, tranh roi xuong nuoc, song sot den man cuoi va cham vao vien ngoc `Emerald` de hoan thanh game.

## Dieu khien

- `A` / `D`: di chuyen trai / phai
- `Space`: nhay
- `Enter`: tan cong hoac chon muc trong menu
- `W` / `S`: di chuyen len / xuong trong menu
- `Esc`: thoat man phu, quay lai menu hoac mo logic tam dung tuy man hinh
- `Backspace`: xoa ky tu khi nhap ten

## Cong nghe su dung

- Ngon ngu: `Java`
- Thu vien do hoa: `Swing`, `AWT`, `Java2D`
- To chuc man choi: doc map `.tmx` tu Tiled
- Am thanh: phat nhac nen va hieu ung bang Java sound
- Dich vu diem so: `Supabase REST API`

## Cau truc thu muc chinh

- `src/`: ma nguon Java
- `src/Main.java`: diem khoi chay chuong trinh
- `src/com/java/WhisperOfTheEmeraldForest/`: ma nguon chinh cua game
- `assets/`: hinh anh, map, sprite, am thanh
- `run.bat`: script compile va chay nhanh tren Windows
- `out/`: file `.class` sau khi bien dich

## Project Structure

```text
Game2d/
|- src/
|  |- Main.java
|  `- com/java/WhisperOfTheEmeraldForest/
|     |- Core.java
|     |- GamePanel.java
|     |- entities/
|     |- input/
|     |- screens/
|     |- services/
|     |- tiled/
|     `- util/
|- assets/
|- out/
|- run.bat
`- README.md
```

- `Main.java`: entry point, goi `Core.main(...)`
- `Core.java`: quan ly cua so game, chuyen man hinh, timer, resolution va leaderboard
- `GamePanel.java`: game loop, render buffer va nhan input
- `entities/`: cac doi tuong trong game nhu `Player`, `OrcEnemy`, `HealthPotion`
- `input/`: quan ly trang thai phim bam
- `screens/`: cac man hinh nhu start menu, gameplay, death, pause va ending
- `services/`: xu ly leaderboard va giao tiep Supabase
- `tiled/`: doc file `.tmx`, tileset va render map tile-based
- `util/`: tien ich ho tro animation, camera, am thanh, text va load asset
- `assets/`: tai nguyen hinh anh, map, sprite sheet, nhac nen va sound effect
- `out/`: thu muc chua file bien dich

## Cach chay du an

### Cach 1: chay nhanh bang file batch

Tren Windows, chay:

```bat
run.bat
```

Script nay se:

1. Bien dich ma nguon tu `src/` vao thu muc `out/`
2. Chay game bang lop `Main`

### Cach 2: chay bang lenh Java

```bat
javac -d out -sourcepath src src\Main.java
java -cp out Main
```

## Yeu cau moi truong

- Cai `JDK` va co san `javac`, `java` trong `PATH`
- He dieu hanh Windows la phu hop nhat vi du an co dung font he thong tai `C:/Windows/Fonts/segoeui.ttf`

## Co che gameplay

1. Mo game va vao menu chinh
2. Chon `Play`
3. Nhap ten nguoi choi
4. Vuot qua man 1
5. Tieu diet het dich de duoc phep sang man tiep theo
6. Vuot qua man 2
7. Sang man cuoi, cham vao vien ngoc `Emerald`
8. He thong luu thoi gian hoan thanh va hien thi ket qua

## Mot so diem ky thuat dang chu y

- Game dung do phan giai ao `800 x 480` de giu bo cuc on dinh
- Co cac che do hien thi `800x480`, `1200x720`, `1600x960` va `Full Screen`
- Camera bam theo nhan vat trong luc choi
- Va cham duoc xu ly thu cong theo tile
- Leaderboard duoc tai va gui diem bat dong bo de tranh chan giao dien

## Leaderboard

Game hien co tich hop san thong tin ket noi Supabase trong ma nguon. Khi co Internet, game co the:

- tai danh sach diem cao
- gui thoi gian hoan thanh sau khi nguoi choi chien thang

Neu khong co mang hoac dich vu khong phan hoi, phan leaderboard co the khong cap nhat nhung game van co the chay phan choi offline.

## Thanh vien thuc hien

- Nguyen Duc Truong - 241230872
- Nguyen Khac Minh - 241224482
- Tran Huu Long - 241230780

## Tom tat

Day la bai lam game 2D platformer bang Java thuan, tap trung vao cac thanh phan cot loi cua mot game desktop: menu, nhan vat, ke dich, ban do tile-based, va cham, am thanh, nhieu man choi, man hinh ket thuc va luu bang xep hang truc tuyen.
