/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.itasoft.tmmin.eform.am.plugin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginWebSupport;
import org.joget.workflow.model.service.WorkflowUserManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Tarkiman
 */
public class LoadDataEmployee extends Element implements PluginWebSupport {

    public static String pluginName = "TMMIN - AM - Load Data Employee";

    @Override
    public String renderTemplate(FormData fd, Map map) {
        return "";
    }

    public String getName() {
        return pluginName;

    }

    public String getVersion() {
        return "1.0.0";
    }

    public String getDescription() {
        return pluginName;
    }

    public String getLabel() {
        return pluginName;
    }

    public String getClassName() {
        return this.getClass().getName();
    }

    public String getPropertyOptions() {
        return "";
    }
//the method calculates the age  

    public static int calculateAge(LocalDate dob) {
        //creating an instance of the LocalDate class and invoking the now() method      
        //now() method obtains the current date from the system clock in the default time zone      
        LocalDate curDate = LocalDate.now();
        //calculates the amount of time between two dates and returns the years  
        if ((dob != null) && (curDate != null)) {
            return Period.between(dob, curDate).getYears();
        } else {
            return 0;
        }
    }

    public void webService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        WorkflowUserManager workflowUserManager = (WorkflowUserManager) AppUtil.getApplicationContext().getBean("workflowUserManager");

        JSONObject mainObj = new JSONObject();

        /*Cek Hanya User Yang Sudah Login Bisa Mengakses Ini*/
        if (!workflowUserManager.isCurrentUserAnonymous()) {

            if (request.getParameterMap().containsKey("username")) {

                String username = request.getParameter("username");

                DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
                StringBuilder query = new StringBuilder();
                query
                        .append("SELECT ")
                        .append("a.c_username AS username, ")
                        .append("a.c_class_cd AS class_cd, ")
                        .append("a.c_noreg AS noreg, ")
                        .append("a.c_name AS name, ")
                        .append("a.c_class AS class, ")
                        .append("a.c_email AS email, ")
                        .append("a.c_position_abbr AS position_abbr, ")
                        .append("a.c_labor_type AS labor_type, ")
                        .append("a.c_div_id AS div_id, ")
                        .append("a.c_div_name AS div_name, ")
                        .append("a.c_section_id AS section_id, ")
                        .append("a.c_section_name AS section_name, ")
                        .append("a.c_dept_id AS dept_id, ")
                        .append("a.c_dept_name AS dept_name, ")
                        .append("a.c_cost_center AS cost_center, ")
                        .append("a.c_ext AS ext, ")
                        .append("a.c_status AS present_status, ")
                        .append("a.c_entry_date AS date_of_employee, ")
                        .append("a.c_date_of_birth AS date_of_birth, ")
                        .append("a.c_labor_type AS labor_type, ")
                        .append("CASE WHEN a.c_date_of_birth  IS NULL THEN '-' WHEN a.c_date_of_birth = '' THEN '-' ELSE a.c_date_of_birth END AS dob, ")
                        .append("CONVERT(date,DATEADD(day,-1,(DATEADD(year,1,a.c_entry_date)))) AS expired_date  ")
                        .append("FROM app_fd_employee a WITH(NOLOCK) ")
                        .append("WHERE a.c_username = ? ");

                try (
                        Connection con = ds.getConnection();
                        PreparedStatement ps = con.prepareStatement(query.toString())) {
                    ps.setString(1, username);

                    try (ResultSet rs = ps.executeQuery()) {

                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();

                        JSONArray list = new JSONArray();
                        while (rs.next()) {
                            JSONObject valObject = new JSONObject();
                            for (int i = 1; i <= columnCount; i++) {
                                valObject.put(rsmd.getColumnLabel(i), rs.getString(rsmd.getColumnLabel(i)));
                            }
                            String str_dob = rs.getString("date_of_birth");
                            LocalDate dob = LocalDate.parse(str_dob);  
                            valObject.put("age", ""+calculateAge(dob));                            
                            list.put(valObject);
                        }
                        mainObj.put("data", list);
                        mainObj.write(response.getWriter());

                    } catch (JSONException ex) {
                        LogUtil.error(this.getClass().getName(), ex, "Error : " + ex.getMessage());
                    }

                } catch (SQLException e) {
                    LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
                }
            } else if (request.getParameterMap().containsKey("noreg")) {

                String noreg = request.getParameter("noreg");

                DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
                StringBuilder query = new StringBuilder();
                query
                        .append("SELECT ")
                        .append("a.c_username AS username, ")
                        .append("a.c_class_cd AS class_cd, ")
                        .append("a.c_noreg AS noreg, ")
                        .append("a.c_name AS name, ")
                        .append("a.c_class AS class, ")
                        .append("a.c_email AS email, ")
                        .append("a.c_position_abbr AS position_abbr, ")
                        .append("a.c_labor_type AS labor_type, ")
                        .append("a.c_div_id AS div_id, ")
                        .append("a.c_div_name AS div_name, ")
                        .append("a.c_section_id AS section_id, ")
                        .append("a.c_section_name AS section_name, ")
                        .append("a.c_dept_id AS dept_id, ")
                        .append("a.c_dept_name AS dept_name, ")
                        .append("a.c_cost_center AS cost_center, ")
                        .append("a.c_ext AS ext, ")
                        .append("a.c_status AS present_status, ")
                        .append("a.c_entry_date AS date_of_employee, ")
                        .append("CASE WHEN a.c_date_of_birth  IS NULL THEN '-' WHEN a.c_date_of_birth = '' THEN '-' ELSE a.c_date_of_birth END AS dob, ")
                        .append("CONVERT(date,DATEADD(day,-1,(DATEADD(year,1,a.c_entry_date)))) AS expired_date  ")
                        .append("FROM app_fd_employee a WITH(NOLOCK) ")
                        .append("WHERE a.c_noreg = ? ");

                try (
                        Connection con = ds.getConnection();
                        PreparedStatement ps = con.prepareStatement(query.toString())) {
                    ps.setString(1, noreg);

                    try (ResultSet rs = ps.executeQuery()) {

                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();

                        JSONArray list = new JSONArray();
                        while (rs.next()) {
                            JSONObject valObject = new JSONObject();
                            for (int i = 1; i <= columnCount; i++) {
                                valObject.put(rsmd.getColumnLabel(i), rs.getString(rsmd.getColumnLabel(i)));
                            }
                            list.put(valObject);
                        }
                        mainObj.put("data", list);
                        mainObj.write(response.getWriter());

                    } catch (JSONException ex) {
                        LogUtil.error(this.getClass().getName(), ex, "Error : " + ex.getMessage());
                    }

                } catch (SQLException e) {
                    LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
                }
            }
        } else {

            try {
                mainObj.put("status", false);
                mainObj.put("message", "You Must Login First.");
                mainObj.write(response.getWriter());
            } catch (JSONException ex) {
                LogUtil.error(this.getClass().getName(), ex, "Error : " + ex.getMessage());
            }
        }
    }

}
