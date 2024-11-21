/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.co.itasoft.tmmin.eform.am.plugin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
 * @author asus
 */
public class LoadDataCaScore extends Element implements PluginWebSupport {

    public static String pluginName = "TMMIN - AM - Load Data CA Score";
    
    @Override
    public String renderTemplate(FormData fd, Map map) {
        return "";
    }

    @Override
    public String getName() {
        return pluginName;
        
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getDescription() {
        return pluginName;
    }

    @Override
    public String getLabel() {
        return pluginName;
    }

    @Override
    public String getClassName() {
        return this.getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return "";
    }

    @Override
    public void webService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        WorkflowUserManager workflowUserManager = (WorkflowUserManager) AppUtil.getApplicationContext().getBean("workflowUserManager");       

        JSONObject mainObj = new JSONObject();
            
        /*Cek Hanya User Yang Sudah Login Bisa Mengakses Ini*/
        if (!workflowUserManager.isCurrentUserAnonymous()) {
        
        
            if (request.getParameterMap().containsKey("noreg")) {

                String noreg = request.getParameter("noreg");

                DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
                StringBuilder query = new StringBuilder();
                query
                    .append("SELECT ")
                    .append("TOP 3")
                    .append("a.c_noreg, ")
                    .append("a.c_service_year_company, ")
                    .append("a.c_service_year_class, ")
                    .append("a.c_ca_year, ")
                    .append("a.c_ca_score ")
                    .append("FROM app_fd_am_ca_score a WITH(NOLOCK) ")
                    .append("WHERE a.c_noreg=? ")
                    .append("ORDER BY a.c_ca_year DESC");
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
        }
        else{
            
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