/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.KienNghi;
import models.NguoiDan;
import models.PhanHoiForUser;
import models.User;
import services.MySqlConnection;
import services.NguoiDanService;
import services.PhanHoiService;
import services.UserService;

/**
 *
 * @author ACER
 */
public class LoginController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();
        
        try {
            int userID = UserService.validateLogin(username, password);
            if (userID != -1 && userID != 0) {
                HttpSession session = request.getSession();
                User user = new User();
                user.setID(userID);
                user.setUserName(username);
                user.setPasswd(password);
                List<KienNghi> listKNPhanHoi = PhanHoiService.getKienNghiPhanHoi(userID);
                request.setAttribute("listPH", listKNPhanHoi);
                session.setAttribute("userID", userID);
                request.setAttribute("user", user);
                
                NguoiDan nd = NguoiDanService.getNguoiDan(userID);
                request.setAttribute("nguoiDan", nd);
                request.setAttribute("hoTen", nd.getHoTen());
                request.setAttribute("diaChi", nd.getDiaChi());
                request.setAttribute("sdt", nd.getSoDienThoai());
                request.setAttribute("email", nd.getEmail());
                request.setAttribute("gioiTinh", nd.getGioiTinh());
                request.setAttribute("cmnd", nd.getCmnd());
                
                response.setContentType("text/html;charset=UTF-8");
                request.setCharacterEncoding("UTF-8");

                request.getRequestDispatcher("userpage.jsp").forward(request, response);
            }
            else if (userID == 0) {
                response.sendRedirect("adminpage.jsp");
            }
            else {
                response.sendRedirect("login.jsp?err=1");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
}
