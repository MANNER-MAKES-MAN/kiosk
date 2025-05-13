package DAO;
import Other.TTS;

import java.util.List;
import java.util.Scanner;

public class TestSP {
	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("========================");
            System.out.println("1. 저장 | 2. 검색 | 3. 수정 | 4. 삭제 | 5. 종료");
            System.out.println("========================");
            System.out.print("번호를 입력하세요: ");

            String input = sc.nextLine();

            switch (input) {
            case "1":
                System.out.println("[메뉴 저장]");
                System.out.print("메뉴 이름: ");
                String name = sc.nextLine();

                System.out.print("가격: ");
                int price = Integer.parseInt(sc.nextLine());

                System.out.print("카테고리: ");
                String category = sc.nextLine();
                
                System.out.print("이미지 설정: ");
                String imagePath = sc.nextLine();

				Menu menu = new Menu(name, price, category, imagePath);
                MenuDAO dao = new MenuDAO();
                dao.insert(menu);

                System.out.println("메뉴 저장이 완료되었습니다.");
                break;
                case "2":
                	System.out.println("[메뉴 전체 조회]");
                    List<Menu> menuList = new MenuDAO().selectAll();

                    for (int i = 0; i < menuList.size(); i++) {
                        Menu m = menuList.get(i);
                        System.out.printf("%d. %s | %d원 | %s\n", i + 1, m.getName(), m.getPrice(), m.getCategory());
                    }

                    if (!menuList.isEmpty()) {
                        boolean subRunning = true;
                        while (subRunning) {
                            System.out.println("\n--- TTS 옵션 선택 ---");
                            System.out.println("1. TTS 음성파일 생성");
                            System.out.println("2. 음성파일 재생");
                            System.out.println("3. 메인화면으로 돌아가기");
                            System.out.print("선택: ");
                            String subChoice = sc.nextLine();

                            switch (subChoice) {
                                case "1":
                                    System.out.println("[TTS 음성파일 전체 생성]");
                                    TTS.Create();  // Python에서 자체 DB 조회 
                                    				//==> 다른 환경이면 파이썬 경로도 수정해야 됨
                                    System.out.println("TTS 음성파일 생성이 완료되었습니다.");
                                    break;
                                case "2":
                                	System.out.print("재생할 메뉴 번호 선택: ");
                                    int playIdx = Integer.parseInt(sc.nextLine()) - 1;
                                    if (playIdx >= 0 && playIdx < menuList.size()) {
                                        String audioFile = menuList.get(playIdx).getAudioGuide();
                                        TTS.play(audioFile);
                                    } else {
                                        System.out.println("잘못된 번호입니다.");
                                    }
                                    break;
                                case "3":
                                    subRunning = false;
                                    break;
                                default:
                                    System.out.println("올바른 번호를 입력하세요.");
                            }
                        }
                    } else {
                        System.out.println("저장된 메뉴가 없습니다.");
                    }
                    break;
                case "3":
                	System.out.println("[수정 기능 실행]");
                	System.out.print("검색할 테이블 기본키: ");
                	String updateID = sc.nextLine();
                    System.out.print("수정할 메뉴 이름: ");
                    String updateName = sc.nextLine();

                    System.out.print("새 가격: ");
                    int newPrice = Integer.parseInt(sc.nextLine());

                    System.out.print("새 카테고리: ");
                    String newCategory = sc.nextLine();

                    System.out.print("새 이미지 경로 (없으면 Enter): ");
                    String newImagePath = sc.nextLine();

                    System.out.print("재고 수정값: ");
                    int newStock = Integer.parseInt(sc.nextLine());

                    Menu updateMenu = new Menu(updateName, newPrice, newCategory, newImagePath);
                    updateMenu.setMenuID(updateID);
                    updateMenu.setStock(newStock);

                    int updateResult = new MenuDAO().update(updateMenu);
                    if (updateResult == 1) {
                        System.out.println("수정 성공");
                    } else if (updateResult == -1) {
                        System.out.println("재고는 0 이상이어야 합니다");
                    } else {
                        System.out.println("수정 실패 또는 메뉴가 존재하지 않음");
                    }
                    break;
                case "4":
                	System.out.println("[삭제 기능 실행]");
                    System.out.print("삭제할 메뉴 이름: ");
                    String deleteName = sc.nextLine();

                    int deleteResult = new MenuDAO().delete(deleteName);
                    if (deleteResult == 1) {
                        System.out.println("삭제 성공");
                    } else {
                        System.out.println("삭제 실패 또는 메뉴 없음");
                    }
                    break;
                case "5":
                    System.out.println("프로그램을 종료합니다.");
                    running = false;
                    break;
                default:
                    System.out.println("올바른 번호를 입력하세요.");
            }
            System.out.println(); // 줄바꿈
        }

        sc.close();
    }
}
