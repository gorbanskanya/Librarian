package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class User 
{
   static
   {
      try
      {
         Class.forName("oracle.jdbc.driver.OracleDriver");   
      } catch (ClassNotFoundException cnfe)
      {
         cnfe.printStackTrace();
      }
   }
         
   Connection con;
   PreparedStatement psmt1;
   PreparedStatement psmt2;
   PreparedStatement psmt3;
   PreparedStatement psmt4;
   PreparedStatement psmt5;
         
   Scanner sc = new Scanner(System.in);
         
   public static void main(String[] args) 
   {
            
      User smu = new User();        
      smu.userRun();         
   }
      
      public void userRun()
      {
         connectDatabase();
         int choice;
         while (true)
         {
            showU();
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice)
            {
            case 1:
               addUser();
               break;
            case 2:
               selUser();
               break;
            case 3:
               adtUser();
               break;
            case 4:
               delUser();
               break;
            case 5:
               allUser();
               break;
            case 6:
               System.out.println("프로그램을 종료합니다");
               return;
            default:
               System.out.println("잘못입력하셨습니다.");
               break;                  
            }
         }
      }
      
      public void showU()
      {
         System.out.println("[회원 정보]");
         System.out.print("1. 회원 가입  ");
         System.out.print("2. 회원 조회  ");
         System.out.print("3. 회원 정보 수정  ");
         System.out.print("4. 회원 탈퇴  ");
         System.out.print("5. 회원 전체 조회  ");
         System.out.println("6. 종료");
      }
      
      public void addUser()
      {
         System.out.println("이름 : ");
         String name = sc.nextLine();
         System.out.println("전화번호(필수입력) : "); 
         String phone_number = sc.nextLine();
         
         try
         {
            psmt1.setString(1, name);
            psmt1.setString(2, "일반");
            psmt1.setString(3, phone_number);
            psmt1.setString(4, "n");
            psmt1.setInt(5, 2);
            psmt1.setInt(6, 2);
            psmt1.setInt(7, 0);
            psmt1.setInt(8, 0);
            
            int updateCount = psmt1.executeUpdate();
            
            System.out.println("**저장 완료되었습니다**");
            System.out.println();
            System.out.print("회원이름 : " + name + " / ");
            System.out.println("등급 : 일반");
            System.out.print("전화번호 : " + phone_number + " / ");
            System.out.println("블랙여부 : ");
            System.out.print("최대대여 : 2 / ");
            System.out.print("대여가능 : 2 / ");
            System.out.print("대여중 : 0 / ");
            System.out.println("대여횟수 : 0");
            System.out.println();
         }
         catch (Exception e)
         {
            System.out.println("**오류! 이름 또는 전화번호가 입력되지 않았습니다**");
         }
      }
      
      public void selUser()
      {
         System.out.println("**조회할 회원의 이름을 입력하고 엔터를 눌러주세요**");
         String name = sc.nextLine();
         
         try
         {
            psmt2.setString(1, name);
            ResultSet rs = psmt2.executeQuery();
            if(rs.next())
            {
               System.out.println("**조회 결과입니다**");
               System.out.print("회원이름 : " + rs.getString(1) + " / ");
               System.out.println("등급 : " + rs.getString(2));
               System.out.print("전화번호 : " + rs.getString(3) + " / ");
               System.out.println("블랙여부 : " + rs.getString(4));
               System.out.print("최대대여 : " + rs.getInt(5) + " / ");
               System.out.print("대여가능 : " + rs.getInt(6) + " / ");
               System.out.print("대여중 : " + rs.getInt(7) + "/ ");
               System.out.println("대여횟수 : " + rs.getInt(8));
               System.out.println();
            } else
               System.out.println("**존재하지 않는 이름입니다**");
            rs.close();
         }
         catch(Exception e)
         {
            System.out.println("알 수 없는 에러가 발생했습니다.");
         }
      }
      
      public void adtUser()
      {
         try
         {
            System.out.println("**수정할 회원의 이름을 입력하세요**");
            String adtName = sc.nextLine();
            System.out.println("수정할 항목을 선택하세요. 1.이름 2.전화번호");
            String adtNum = sc.nextLine();
            switch(adtNum)
            {
            case "1":
               System.out.print("새로 수정할 이름을 입력하세요");
               String nName = sc.nextLine();
               
               psmt3.setString(1, nName);
               psmt3.setString(2, adtName);
               int result1 = psmt3.executeUpdate();
               if(result1 == 1)
                  System.out.println("수정 성공");
               else
                  System.out.println("수정 실패");
               break;
            case "2":
               System.out.print("새로 수정할 전화번호를 입력하세요");
               String nPhone = sc.nextLine();
               psmt4.setString(1, nPhone);
               psmt4.setString(2, adtName);
               int result2 = psmt4.executeUpdate();
               if(result2 == 1)
                  System.out.println("수정 성공");
               else
                  System.out.println("수정 실패");
               break;
            }
         }
         catch(SQLException sqle)
         {
            sqle.printStackTrace();
         }
      }
      
      public void delUser()
      {
         System.out.println("**삭제할 이름을 입력하고 엔터를 눌러주세요**");
         String name = sc.nextLine();
         
         try
         {
            psmt5.setString(1, name);
            int updateCount = psmt5.executeUpdate();
            System.out.println("데이터베이스에서 삭제되었습니다");
         }
         catch (Exception e)
         {
            System.out.println("데이터베이스 삭제 에러입니다");
         }
      }
      
      public void allUser()
      {
         try
         {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
                  "Librarian", "1234");
            Statement stmt = con.createStatement();
            
            StringBuffer sb = new StringBuffer();
            sb.append("select * from UserDB");
            
            ResultSet rs = stmt.executeQuery(sb.toString());
            while (rs.next())
            {
               System.out.println();
               System.out.print("회원이름 : " + rs.getString(1) + " / ");
               System.out.println("등급 : " + rs.getString(2));
               System.out.print("전화번호 : " + rs.getString(3) + " / ");
               System.out.println("블랙여부 : " + rs.getString(4));
               System.out.print("최대대여 : " + rs.getInt(5) + " / ");
               System.out.print("대여가능 : " + rs.getInt(6) + " / ");
               System.out.print("대여중 : " + rs.getInt(7) + " / ");
               System.out.println("대여횟수 : " + rs.getInt(8));
               System.out.println();
            }
            
            rs.close();
            stmt.close();
            con.close();
         }
         catch(SQLException sqle)
         {
            System.out.println("Connection Error");
            sqle.printStackTrace();
         }
      }
      
      public void connectDatabase()
      {
         try
         {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "Librarian", "1234");
         
            String sqlInsert = "insert into UserDB values(?, ?, ?, ?, ?, ?, ?, ?)";
            psmt1 = con.prepareStatement(sqlInsert);
            
            String sqlSelect = "select * from UserDB where 이름 = ?";
            psmt2 = con.prepareStatement(sqlSelect);
            
            String sqlAdtName = "update UserDB set  이름 = ? where 이름 = ?";
            psmt3 = con.prepareStatement(sqlAdtName);
            
            String sqlAdtPhone = "update UserDB set 전화번호 = ? where 이름 = ?";
            psmt4 = con.prepareStatement(sqlAdtPhone);
            
            String sqlDelete = "delete from UserDB where 이름 = ?";
            psmt5 = con.prepareStatement(sqlDelete);
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }
