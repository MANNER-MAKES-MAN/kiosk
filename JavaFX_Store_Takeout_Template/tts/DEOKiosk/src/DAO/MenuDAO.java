package DAO;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
	//삽입 (성공 시 1 반환, 중복된 메뉴 이름일 시 -1, 그 외의 오류일 시 0을 반환)
	 public int insert(Menu menu) {
		 String checkSql = "SELECT COUNT(*) FROM Menu WHERE Name = ?";
		    String insertSql = "INSERT INTO Menu (Name, Price, Stock, ImagePath, Category) VALUES (?, ?, ?, ?, ?)";

		    try (
		        Connection conn = DBConnect.getConnection();
		        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
		        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
		    ) {
		        // 1. 메뉴 이름 중복 체크
		        checkStmt.setString(1, menu.getName());
		        ResultSet rs = checkStmt.executeQuery();
		        if (rs.next() && rs.getInt(1) > 0) {
		            System.out.println("[오류] 중복된 메뉴 이름입니다: " + menu.getName());
		            return -1;
		        }

		        // 2. 실제 삽입 진행
		        insertStmt.setString(1, menu.getName());
		        insertStmt.setInt(2, menu.getPrice());
		        insertStmt.setInt(3, menu.getStock());

		        // ImagePath는 null 또는 빈 문자열일 수 있음
		        if (menu.getImagePath() == null || menu.getImagePath().trim().isEmpty()) {
		            insertStmt.setNull(4, java.sql.Types.VARCHAR);
		        } else {
		            insertStmt.setString(4, menu.getImagePath());
		        }

		        insertStmt.setString(5, menu.getCategory());

		        int result = insertStmt.executeUpdate();

		        if (result > 0) {
		            System.out.println("[성공] 메뉴 저장 완료: " + menu.getName());
		            return 1;
		        } else {
		            System.out.println("[실패] 메뉴 저장 실패");
		            return 0;
		        }

		    } catch (Exception e) {
		        System.out.println("[예외] DB 삽입 중 오류 발생:");
		        e.printStackTrace();
		        return 0;
		    }
	    }
	 //전체 정보 조회(이미지파일명은 제외)
	 public List<Menu> selectAll() {
		    List<Menu> list = new ArrayList<>();
		    String sql = "SELECT * FROM Menu";

		    try (
		        Connection conn = DBConnect.getConnection();
		        PreparedStatement pstmt = conn.prepareStatement(sql);
		        ResultSet rs = pstmt.executeQuery();
		    ) {
		        while (rs.next()) {
		            Menu menu = new Menu();
		            menu.setMenuID(rs.getString("MenuID"));
		            menu.setName(rs.getString("Name"));
		            menu.setPrice(rs.getInt("Price"));
		            menu.setStock(rs.getInt("Stock"));
		            menu.setImagePath(rs.getString("ImagePath"));
		            menu.setAudioGuide(rs.getString("AudioGuide"));
		            menu.setCategory(rs.getString("Category"));
		            list.add(menu);
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return list;
		}
	 //수정 재고가 음수
	 public int update(Menu menu) {
		    // 유효성 검사: 재고 음수 불가
		    if (menu.getStock() < 0) {
		        System.out.println("[오류] 재고는 음수일 수 없습니다.");
		        return -1;
		    }

		    // 중복 이름 검사 (수정 대상 외에 동일한 이름이 존재하는지 확인)
		    String checkSql = "SELECT COUNT(*) FROM Menu WHERE Name = ? AND MenuID != ?";
		    String updateSql = "UPDATE Menu SET Name = ?, Price = ?, Category = ?, Stock = ?, ImagePath = ? WHERE MenuID = ?";

		    try (
		        Connection conn = DBConnect.getConnection();
		        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
		        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
		    ) {
		        // 이름 중복 확인
		        checkStmt.setString(1, menu.getName());
		        checkStmt.setString(2, menu.getMenuID());
		        ResultSet rs = checkStmt.executeQuery();
		        if (rs.next() && rs.getInt(1) > 0) {
		            System.out.println("[오류] 동일한 이름의 메뉴가 이미 존재합니다.");
		            return 0;
		        }

		        // 수정 수행
		        updateStmt.setString(1, menu.getName());       // 변경될 이름
		        updateStmt.setInt(2, menu.getPrice());
		        updateStmt.setString(3, menu.getCategory());
		        updateStmt.setInt(4, menu.getStock());

		        if (menu.getImagePath() == null || menu.getImagePath().trim().isEmpty()) {
		            updateStmt.setNull(5, java.sql.Types.VARCHAR);
		        } else {
		            updateStmt.setString(5, menu.getImagePath());
		        }

		        updateStmt.setString(6, menu.getMenuID());     // 수정 대상 기준

		        int result = updateStmt.executeUpdate();

		        if (result > 0) {
		            System.out.println("[성공] 메뉴 수정 완료: " + menu.getName());
		            return 1;
		        } else {
		            System.out.println("[실패] 메뉴 수정에 실패했습니다.");
		            return 0;
		        }

		    } catch (Exception e) {
		        System.out.println("[예외] 메뉴 수정 중 오류 발생:");
		        e.printStackTrace();
		        return 0;
		    }
		}
	 
	 public int delete(String name) {
		    String selectSql = "SELECT AudioGuide FROM Menu WHERE Name = ?";
		    String deleteSql = "DELETE FROM Menu WHERE Name = ?";

		    try (
		        Connection conn = DBConnect.getConnection();
		        PreparedStatement selectStmt = conn.prepareStatement(selectSql);
		        PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
		    ) {
		        // 1. 삭제 전 TTS 음성파일명 가져오기
		        selectStmt.setString(1, name);
		        ResultSet rs = selectStmt.executeQuery();

		        String audioGuide = null;
		        if (rs.next()) {
		            audioGuide = rs.getString("AudioGuide");
		        } else {
		            System.out.println("[실패] 해당 이름의 메뉴가 존재하지 않습니다.");
		            return 0;
		        }

		        // 2. DB에서 메뉴 삭제
		        deleteStmt.setString(1, name);
		        int result = deleteStmt.executeUpdate();

		        if (result > 0) {
		            System.out.println("[성공] 메뉴 삭제 완료: " + name);

		            // 3. 음성 파일 삭제
		            if (audioGuide != null && !audioGuide.trim().isEmpty()) {
		                File file = new File("audio_cache/" + audioGuide);
		                if (file.exists() && file.delete()) {
		                    System.out.println("[성공] 음성파일 삭제 완료: " + audioGuide);
		                } else {
		                    System.out.println("[주의] 음성파일이 존재하지 않거나 삭제 실패: " + audioGuide);
		                }
		            }

		            return 1;
		        } else {
		            System.out.println("[실패] 메뉴 삭제에 실패했습니다.");
		            return 0;
		        }

		    } catch (Exception e) {
		        System.out.println("[예외] 메뉴 삭제 중 오류 발생:");
		        e.printStackTrace();
		        return 0;
		    }
		}
}
