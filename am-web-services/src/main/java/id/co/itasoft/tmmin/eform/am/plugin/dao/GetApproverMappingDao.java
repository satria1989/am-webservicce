/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.itasoft.tmmin.eform.am.plugin.dao;

import id.co.itasoft.tmmin.eform.am.plugin.model.Approver;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;

/**
 *
 * @author Tarkiman
 */
public class GetApproverMappingDao {

    public Approver getDetailCurrentUserByUsername(String username) {

        Approver app = new Approver();

        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        StringBuilder query = new StringBuilder();
        query
                .append("SELECT ")
                .append("a.c_username, ")
                .append("a.c_class_cd, ")
                .append("a.c_noreg, ")
                .append("a.c_name, ")
                .append("a.c_class, ")
                .append("a.c_email, ")
                .append("a.c_position_abbr, ")
                .append("a.c_labor_type, ")
                .append("a.c_div_id, ")
                .append("a.c_div_name, ")
                .append("a.c_section_id, ")
                .append("a.c_section_name, ")
                .append("a.c_dept_id, ")
                .append("a.c_dept_name, ")
                .append("a.c_cost_center, ")
                .append("a.c_main_location, ")
                .append("a.c_ext, ")
                .append("a.c_status ")
                .append("FROM app_fd_employee a WITH(NOLOCK) ")
                .append("WHERE a.c_username=? ");

        try (
                Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    app.setUsername(rs.getString("c_username"));
                    app.setClassCd(rs.getString("c_class_cd"));
                    app.setNoreg(rs.getString("c_noreg"));
                    app.setName(rs.getString("c_name"));
                    app.setClasS(rs.getString("c_class"));
                    app.setEmail(rs.getString("c_email"));
                    app.setPositionAbbr(rs.getString("c_position_abbr"));
                    app.setLaborType(rs.getString("c_labor_type"));
                    app.setDivId(rs.getString("c_div_id"));
                    app.setDivName(rs.getString("c_div_name"));
                    app.setSectionId(rs.getString("c_section_id"));
                    app.setSectionName(rs.getString("c_section_name"));
                    app.setDeptId(rs.getString("c_dept_id"));
                    app.setDeptName(rs.getString("c_dept_name"));
                    app.setCostCenter(rs.getString("c_cost_center"));
                    app.setMainLocation(rs.getString("c_main_location"));
                    app.setExt(rs.getString("c_ext"));
                    app.setStatus(rs.getString("c_status"));
                }

            }

        } catch (SQLException e) {
            LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
        }

        return app;
    }

    public Approver getDetailCurrentUserByNoreg(String noreg) {

        Approver app = new Approver();

        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        StringBuilder query = new StringBuilder();
        query
                .append("SELECT ")
                .append("a.c_username, ")
                .append("a.c_class_cd, ")
                .append("a.c_noreg, ")
                .append("a.c_name, ")
                .append("a.c_class, ")
                .append("a.c_email, ")
                .append("a.c_position_abbr, ")
                .append("a.c_labor_type, ")
                .append("a.c_div_id, ")
                .append("a.c_div_name, ")
                .append("a.c_section_id, ")
                .append("a.c_section_name, ")
                .append("a.c_dept_id, ")
                .append("a.c_dept_name, ")
                .append("a.c_cost_center, ")
                .append("a.c_main_location, ")
                .append("a.c_ext, ")
                .append("a.c_status ")
                .append("FROM app_fd_employee a WITH(NOLOCK) ")
                .append("WHERE a.c_noreg=? ");

        try (
                Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setString(1, noreg);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    app.setUsername(rs.getString("c_username"));
                    app.setClassCd(rs.getString("c_class_cd"));
                    app.setNoreg(rs.getString("c_noreg"));
                    app.setName(rs.getString("c_name"));
                    app.setClasS(rs.getString("c_class"));
                    app.setEmail(rs.getString("c_email"));
                    app.setPositionAbbr(rs.getString("c_position_abbr"));
                    app.setLaborType(rs.getString("c_labor_type"));
                    app.setDivId(rs.getString("c_div_id"));
                    app.setDivName(rs.getString("c_div_name"));
                    app.setSectionId(rs.getString("c_section_id"));
                    app.setSectionName(rs.getString("c_section_name"));
                    app.setDeptId(rs.getString("c_dept_id"));
                    app.setDeptName(rs.getString("c_dept_name"));
                    app.setCostCenter(rs.getString("c_cost_center"));
                    app.setMainLocation(rs.getString("c_main_location"));
                    app.setExt(rs.getString("c_ext"));
                    app.setStatus(rs.getString("c_status"));
                }

            }

        } catch (SQLException e) {
            LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
        }

        return app;
    }

    public Approver getDetailEmployeeByUsername(String username) {

        Approver app;

        Approver jabatanTertinggi = new Approver();

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
                .append("a.c_status AS present_status,  ")
                .append("a.c_main_location AS main_location,  ")
                .append("a.c_entry_date AS date_of_employee, ")
                .append("a.c_status AS status, ")
                .append("a.c_gender AS gender, ")
                .append("a.c_street_address AS street_address, ")
                .append("a.c_place_of_birth AS place_of_birth, ")
                .append("a.c_directorate_id AS directorate_id, ")
                .append("CASE WHEN b.c_terlambat  IS NULL THEN '-' WHEN b.c_terlambat = '0' THEN '-' ELSE b.c_terlambat END AS terlambat, ")
                .append("CASE WHEN b.c_sakit_opname  IS NULL THEN '-' WHEN b.c_sakit_opname  = '0' THEN '-' ELSE b.c_sakit_opname  END AS opname, ")
                .append("CASE WHEN b.c_sakit_surat_dokter  IS NULL THEN '-' WHEN b.c_sakit_surat_dokter  = '0' THEN '-' ELSE b.c_sakit_surat_dokter  END AS surat_dokter, ")
                .append("CASE WHEN b.c_mangkir  IS NULL THEN '-' WHEN b.c_mangkir  = '0' THEN '-' ELSE b.c_mangkir  END AS mangkir, ")
                .append("CASE WHEN b.c_pulcep_dengan_ijin  IS NULL THEN '-' WHEN b.c_pulcep_dengan_ijin  = '0' THEN '-' ELSE b.c_pulcep_dengan_ijin  END AS pulcep_dengan_ijin, ")
                .append("CASE WHEN b.c_pulcep_tanpa_ijin  IS NULL THEN '-' WHEN b.c_pulcep_tanpa_ijin  = '0' THEN '-' ELSE b.c_pulcep_tanpa_ijin  END AS pulcep_tanpa_ijin, ")
                .append("b.c_unit_code AS unit_code, ")
                .append("CASE WHEN a.c_date_of_birth  IS NULL THEN '-' WHEN a.c_date_of_birth = '' THEN '-' ELSE a.c_date_of_birth END AS dob, ")
                .append("CONVERT(date,DATEADD(day,-1,(DATEADD(year,1,a.c_entry_date)))) AS expired_date ")
                .append("FROM app_fd_employee a WITH(NOLOCK) ")
                .append("LEFT JOIN app_fd_er_presensi b WITH(NOLOCK) ON a.c_noreg = b.c_noreg ")
                .append("WHERE a.c_username = ? ")
                .append("and a.c_position_percent = 100 ");

        try (
                Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    
                    app = new Approver();
                    app.setUsername(rs.getString("username"));
                    app.setClassCd(rs.getString("class_cd"));
                    app.setNoreg(rs.getString("noreg"));
                    app.setName(rs.getString("name"));
                    app.setClasS(rs.getString("class"));
                    app.setEmail(rs.getString("email"));
                    app.setPositionAbbr(rs.getString("position_abbr"));
                    app.setLaborType(rs.getString("labor_type"));
                    app.setDivId(rs.getString("div_id"));
                    app.setDivName(rs.getString("div_name"));
                    app.setSectionId(rs.getString("section_id"));
                    app.setSectionName(rs.getString("section_name"));
                    app.setDeptId(rs.getString("dept_id"));
                    app.setDeptName(rs.getString("dept_name"));
                    app.setCostCenter(rs.getString("cost_center"));
                    app.setExt(rs.getString("ext"));
                    app.setPresentStatus(rs.getString("present_status"));
                    app.setMainLocation(rs.getString("main_location"));
                    app.setDateOfEmployee(rs.getString("date_of_employee"));
                    app.setPlaceOfBirth(rs.getString("place_of_birth"));
                    app.setDateOfBirth(rs.getString("dob"));
                    app.setExpiredDate(rs.getString("expired_date"));
                    app.setGender(rs.getString("gender"));
                    app.setStreetAddress(rs.getString("street_address"));
                    app.setTerlambat(rs.getString("terlambat"));
                    app.setSakitOpname(rs.getString("opname"));
                    app.setSakitSuratDokter(rs.getString("surat_dokter"));
                    app.setMangkir(rs.getString("mangkir"));
                    app.setPulcepDenganIjin(rs.getString("pulcep_dengan_ijin"));
                    app.setPulcepTanpaIjin(rs.getString("pulcep_tanpa_ijin"));
                    app.setStatus(rs.getString("status"));
                    app.setUnitCode(rs.getString("unit_code"));
                    app.setDirectorateId(rs.getString("directorate_id"));

                    /*TAMPUNGAN UNTUK EMPLOYEE YANG RANGKAP JABATAN*/
                    if (jabatanTertinggi.getPositionAbbr() == null) {
                        jabatanTertinggi = app;
                    } else {
                        if (positionInNumber(app.getPositionAbbr()) < 8) { //Hanya DPH dan jabatan di bawahnya, min SH 
                            if (positionInNumber(jabatanTertinggi.getPositionAbbr()) < positionInNumber(app.getPositionAbbr())) {
                                jabatanTertinggi = app;
                            }
                        }
                    }
                }

            }

        } catch (SQLException e) {
            LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
        }

        return jabatanTertinggi;
    }

    public Approver getAnEmployeeBySection(String sectionId) {

        Approver app;

        Approver jabatanTerendah = new Approver();

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
                .append("a.c_status AS present_status,  ")
                .append("a.c_main_location AS main_location,  ")
                .append("a.c_entry_date AS date_of_employee, ")
                .append("a.c_status AS status, ")
                .append("a.c_gender AS gender, ")
                .append("a.c_street_address AS street_address, ")
                .append("a.c_place_of_birth AS place_of_birth, ")
                .append("a.c_directorate_id AS directorate_id, ")
                .append("CASE WHEN b.c_terlambat  IS NULL THEN '-' WHEN b.c_terlambat = '0' THEN '-' ELSE b.c_terlambat END AS terlambat, ")
                .append("CASE WHEN b.c_sakit_opname  IS NULL THEN '-' WHEN b.c_sakit_opname  = '0' THEN '-' ELSE b.c_sakit_opname  END AS opname, ")
                .append("CASE WHEN b.c_sakit_surat_dokter  IS NULL THEN '-' WHEN b.c_sakit_surat_dokter  = '0' THEN '-' ELSE b.c_sakit_surat_dokter  END AS surat_dokter, ")
                .append("CASE WHEN b.c_mangkir  IS NULL THEN '-' WHEN b.c_mangkir  = '0' THEN '-' ELSE b.c_mangkir  END AS mangkir, ")
                .append("CASE WHEN b.c_pulcep_dengan_ijin  IS NULL THEN '-' WHEN b.c_pulcep_dengan_ijin  = '0' THEN '-' ELSE b.c_pulcep_dengan_ijin  END AS pulcep_dengan_ijin, ")
                .append("CASE WHEN b.c_pulcep_tanpa_ijin  IS NULL THEN '-' WHEN b.c_pulcep_tanpa_ijin  = '0' THEN '-' ELSE b.c_pulcep_tanpa_ijin  END AS pulcep_tanpa_ijin, ")
                .append("b.c_unit_code AS unit_code, ")
                .append("CASE WHEN a.c_date_of_birth  IS NULL THEN '-' WHEN a.c_date_of_birth = '' THEN '-' ELSE a.c_date_of_birth END AS dob, ")
                .append("CONVERT(date,DATEADD(day,-1,(DATEADD(year,1,a.c_entry_date)))) AS expired_date ")
                .append("FROM app_fd_employee a WITH(NOLOCK) ")
                .append("LEFT JOIN app_fd_er_presensi b WITH(NOLOCK) ON a.c_noreg = b.c_noreg ")
                .append("WHERE a.c_section_id = ? ")
                .append("and a.c_position_percent = 100 ");

        try (
                Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setString(1, sectionId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    app = new Approver();
                    app.setUsername(rs.getString("username"));
                    app.setClassCd(rs.getString("class_cd"));
                    app.setNoreg(rs.getString("noreg"));
                    app.setName(rs.getString("name"));
                    app.setClasS(rs.getString("class"));
                    app.setEmail(rs.getString("email"));
                    app.setPositionAbbr(rs.getString("position_abbr"));
                    app.setLaborType(rs.getString("labor_type"));
                    app.setDivId(rs.getString("div_id"));
                    app.setDivName(rs.getString("div_name"));
                    app.setSectionId(rs.getString("section_id"));
                    app.setSectionName(rs.getString("section_name"));
                    app.setDeptId(rs.getString("dept_id"));
                    app.setDeptName(rs.getString("dept_name"));
                    app.setCostCenter(rs.getString("cost_center"));
                    app.setExt(rs.getString("ext"));
                    app.setPresentStatus(rs.getString("present_status"));
                    app.setMainLocation(rs.getString("main_location"));
                    app.setDateOfEmployee(rs.getString("date_of_employee"));
                    app.setPlaceOfBirth(rs.getString("place_of_birth"));
                    app.setDateOfBirth(rs.getString("dob"));
                    app.setExpiredDate(rs.getString("expired_date"));
                    app.setGender(rs.getString("gender"));
                    app.setStreetAddress(rs.getString("street_address"));
                    app.setTerlambat(rs.getString("terlambat"));
                    app.setSakitOpname(rs.getString("opname"));
                    app.setSakitSuratDokter(rs.getString("surat_dokter"));
                    app.setMangkir(rs.getString("mangkir"));
                    app.setPulcepDenganIjin(rs.getString("pulcep_dengan_ijin"));
                    app.setPulcepTanpaIjin(rs.getString("pulcep_tanpa_ijin"));
                    app.setStatus(rs.getString("status"));
                    app.setUnitCode(rs.getString("unit_code"));
                    app.setDirectorateId(rs.getString("directorate_id"));

                    /*TAMPUNGAN UNTUK AMBIL JABATAN TERENDAH DI SUATU SECTION*/
                    if (jabatanTerendah.getPositionAbbr() == null) {
                        jabatanTerendah = app;
                    } else {
                        if (positionInNumber(app.getPositionAbbr()) < 8) { //Hanya DPH dan jabatan di bawahnya, min SH 
                            if (positionInNumber(jabatanTerendah.getPositionAbbr()) > positionInNumber(app.getPositionAbbr())) {
                                jabatanTerendah = app;
                            }
                        }
                    }
                }

            }

        } catch (SQLException e) {
            LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
        }

        return jabatanTerendah;
    }

    public Approver getDetailEmployeeByNoreg(String noreg) {

        Approver app = new Approver();

        Approver jabatanTertinggi = new Approver();

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
                .append("a.c_status AS present_status,  ")
                .append("a.c_entry_date AS date_of_employee, ")
                .append("a.c_main_location AS main_location, ")
                .append("a.c_status AS status, ")
                .append("a.c_gender AS gender, ")
                .append("a.c_street_address AS street_address, ")
                .append("a.c_place_of_birth AS place_of_birth, ")
                .append("a.c_directorate_id AS directorate_id, ")
                .append("CASE WHEN b.c_terlambat  IS NULL THEN '-' WHEN b.c_terlambat = '0' THEN '-' ELSE b.c_terlambat END AS terlambat, ")
                .append("CASE WHEN b.c_sakit_opname  IS NULL THEN '-' WHEN b.c_sakit_opname  = '0' THEN '-' ELSE b.c_sakit_opname  END AS opname, ")
                .append("CASE WHEN b.c_sakit_surat_dokter  IS NULL THEN '-' WHEN b.c_sakit_surat_dokter  = '0' THEN '-' ELSE b.c_sakit_surat_dokter  END AS surat_dokter, ")
                .append("CASE WHEN b.c_mangkir  IS NULL THEN '-' WHEN b.c_mangkir  = '0' THEN '-' ELSE b.c_mangkir  END AS mangkir, ")
                .append("CASE WHEN b.c_pulcep_dengan_ijin  IS NULL THEN '-' WHEN b.c_pulcep_dengan_ijin  = '0' THEN '-' ELSE b.c_pulcep_dengan_ijin  END AS pulcep_dengan_ijin, ")
                .append("CASE WHEN b.c_pulcep_tanpa_ijin  IS NULL THEN '-' WHEN b.c_pulcep_tanpa_ijin  = '0' THEN '-' ELSE b.c_pulcep_tanpa_ijin  END AS pulcep_tanpa_ijin, ")
                .append("b.c_unit_code AS unit_code, ")
                .append("CASE WHEN a.c_date_of_birth  IS NULL THEN '-' WHEN a.c_date_of_birth = '' THEN '-' ELSE a.c_date_of_birth END AS dob, ")
                .append("CONVERT(date,DATEADD(day,-1,(DATEADD(year,1,a.c_entry_date)))) AS expired_date ")
                .append("FROM app_fd_employee a WITH(NOLOCK) ")
                .append("LEFT JOIN app_fd_er_presensi b WITH(NOLOCK) ON a.c_noreg = b.c_noreg ")
                .append("WHERE a.c_noreg = ? ")
                .append("and a.c_position_percent = 100 ");

        try (
                Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setString(1, noreg);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    app.setUsername(rs.getString("username"));
                    app.setClassCd(rs.getString("class_cd"));
                    app.setNoreg(rs.getString("noreg"));
                    app.setName(rs.getString("name"));
                    app.setClasS(rs.getString("class"));
                    app.setEmail(rs.getString("email"));
                    app.setPositionAbbr(rs.getString("position_abbr"));
                    app.setLaborType(rs.getString("labor_type"));
                    app.setDivId(rs.getString("div_id"));
                    app.setDivName(rs.getString("div_name"));
                    app.setSectionId(rs.getString("section_id"));
                    app.setSectionName(rs.getString("section_name"));
                    app.setDeptId(rs.getString("dept_id"));
                    app.setDeptName(rs.getString("dept_name"));
                    app.setCostCenter(rs.getString("cost_center"));
                    app.setExt(rs.getString("ext"));
                    app.setPresentStatus(rs.getString("present_status"));
                    app.setMainLocation(rs.getString("main_location"));
                    app.setDateOfEmployee(rs.getString("date_of_employee"));
                    app.setPlaceOfBirth(rs.getString("place_of_birth"));
                    app.setDateOfBirth(rs.getString("dob"));
                    app.setExpiredDate(rs.getString("expired_date"));
                    app.setGender(rs.getString("gender"));
                    app.setStreetAddress(rs.getString("street_address"));
                    app.setTerlambat(rs.getString("terlambat"));
                    app.setSakitOpname(rs.getString("opname"));
                    app.setSakitSuratDokter(rs.getString("surat_dokter"));
                    app.setMangkir(rs.getString("mangkir"));
                    app.setPulcepDenganIjin(rs.getString("pulcep_dengan_ijin"));
                    app.setPulcepTanpaIjin(rs.getString("pulcep_tanpa_ijin"));
                    app.setStatus(rs.getString("status"));
                    app.setUnitCode(rs.getString("unit_code"));
                    app.setDirectorateId(rs.getString("directorate_id"));

                    /*TAMPUNGAN UNTUK EMPLOYEE YANG RANGKAP JABATAN*/
                    if (jabatanTertinggi.getPositionAbbr() == null) {
                        jabatanTertinggi = app;
                    } else {
                        if (positionInNumber(app.getPositionAbbr()) < 8) { //Hanya DPH dan jabatan di bawahnya, min SH 
                            if (positionInNumber(jabatanTertinggi.getPositionAbbr()) < positionInNumber(app.getPositionAbbr())) {
                                jabatanTertinggi = app;
                            }
                        }
                    }
                }

            }

        } catch (SQLException e) {
            LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
        }

        return jabatanTertinggi;
    }

    public Approver getUserApproverSH(String positionAbbr, Approver r) {

        Approver app = new Approver();

        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        StringBuilder query = new StringBuilder();
        query
                .append("SELECT ")
                .append("a.c_username, ")
                .append("a.c_class_cd, ")
                .append("a.c_noreg, ")
                .append("a.c_name, ")
                .append("a.c_class, ")
                .append("a.c_email, ")
                .append("a.c_position_abbr, ")
                .append("a.c_labor_type, ")
                .append("a.c_div_id, ")
                .append("a.c_div_name, ")
                .append("a.c_section_id, ")
                .append("a.c_section_name, ")
                .append("a.c_dept_id, ")
                .append("a.c_dept_name, ")
                .append("a.c_cost_center ")
                .append("FROM app_fd_employee a WITH(NOLOCK) ")
                .append("WHERE a.c_position_abbr=? ")
                .append("AND a.c_div_id=? ")
                .append("AND a.c_dept_id=? ")
                .append("AND a.c_section_id=? ");

        try (
                Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setString(1, positionAbbr);
            ps.setString(2, r.getDivId());
            ps.setString(3, r.getDeptId());
            ps.setString(4, r.getSectionId());

            try (ResultSet rs = ps.executeQuery()) {

                int i = 0;
                while (rs.next()) {

                    if (i > 0) {
                        app.setUsername(app.getUsername() + "," + rs.getString("c_username"));
                        app.setClassCd(app.getClassCd() + "," + rs.getString("c_class_cd"));
                        app.setNoreg(app.getNoreg() + "," + rs.getString("c_noreg"));
                        app.setName(app.getName() + "," + rs.getString("c_name"));
                        app.setClasS(app.getClasS() + "," + rs.getString("c_class"));
                        app.setEmail(app.getEmail() + "," + rs.getString("c_email"));
                        app.setPositionAbbr(app.getPositionAbbr());
                        app.setLaborType(app.getLaborType() + "," + rs.getString("c_labor_type"));
                        app.setDivId(app.getDivId() + "," + rs.getString("c_div_id"));
                        app.setDivName(app.getDivName() + "," + rs.getString("c_div_name"));
                        app.setSectionId(app.getSectionId() + "," + rs.getString("c_section_id"));
                        app.setSectionName(app.getSectionName() + "," + rs.getString("c_section_name"));
                        app.setDeptId(app.getDeptId() + "," + rs.getString("c_dept_id"));
                        app.setDeptName(app.getDeptName() + "," + rs.getString("c_dept_name"));
                        app.setCostCenter(app.getCostCenter() + "," + rs.getString("c_cost_center"));

                    } else {
                        app.setUsername(rs.getString("c_username"));
                        app.setClassCd(rs.getString("c_class_cd"));
                        app.setNoreg(rs.getString("c_noreg"));
                        app.setName(rs.getString("c_name"));
                        app.setClasS(rs.getString("c_class"));
                        app.setEmail(rs.getString("c_email"));
                        app.setPositionAbbr(rs.getString("c_position_abbr"));
                        app.setLaborType(rs.getString("c_labor_type"));
                        app.setDivId(rs.getString("c_div_id"));
                        app.setDivName(rs.getString("c_div_name"));
                        app.setSectionId(rs.getString("c_section_id"));
                        app.setSectionName(rs.getString("c_section_name"));
                        app.setDeptId(rs.getString("c_dept_id"));
                        app.setDeptName(rs.getString("c_dept_name"));
                        app.setCostCenter(rs.getString("c_cost_center"));

                    }
                    i++;

                }

            }

        } catch (SQLException e) {
            LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
        }

        return app;
    }

    public Approver getUserApproverDPH(String positionAbbr, Approver r) {

        Approver app = new Approver();

        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        StringBuilder query = new StringBuilder();
        query
                .append("SELECT ")
                .append("a.c_username, ")
                .append("a.c_class_cd, ")
                .append("a.c_noreg, ")
                .append("a.c_name, ")
                .append("a.c_class, ")
                .append("a.c_email, ")
                .append("a.c_position_abbr, ")
                .append("a.c_labor_type, ")
                .append("a.c_div_id, ")
                .append("a.c_div_name, ")
                .append("a.c_section_id, ")
                .append("a.c_section_name, ")
                .append("a.c_dept_id, ")
                .append("a.c_dept_name, ")
                .append("a.c_cost_center ")
                .append("FROM app_fd_employee a WITH(NOLOCK) ")
                .append("WHERE a.c_position_abbr=? ")
                .append("AND a.c_div_id=? ")
                .append("AND a.c_dept_id=? ");

        try (
                Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setString(1, positionAbbr);
            ps.setString(2, r.getDivId());
            ps.setString(3, r.getDeptId());

            try (ResultSet rs = ps.executeQuery()) {

                int i = 0;
                while (rs.next()) {

                    if (i > 0) {
                        app.setUsername(app.getUsername() + "," + rs.getString("c_username"));
                        app.setClassCd(app.getClassCd() + "," + rs.getString("c_class_cd"));
                        app.setNoreg(app.getNoreg() + "," + rs.getString("c_noreg"));
                        app.setName(app.getName() + "," + rs.getString("c_name"));
                        app.setClasS(app.getClasS() + "," + rs.getString("c_class"));
                        app.setEmail(app.getEmail() + "," + rs.getString("c_email"));
                        app.setPositionAbbr(app.getPositionAbbr());
                        app.setLaborType(app.getLaborType() + "," + rs.getString("c_labor_type"));
                        app.setDivId(app.getDivId() + "," + rs.getString("c_div_id"));
                        app.setDivName(app.getDivName() + "," + rs.getString("c_div_name"));
                        app.setSectionId(app.getSectionId() + "," + rs.getString("c_section_id"));
                        app.setSectionName(app.getSectionName() + "," + rs.getString("c_section_name"));
                        app.setDeptId(app.getDeptId() + "," + rs.getString("c_dept_id"));
                        app.setDeptName(app.getDeptName() + "," + rs.getString("c_dept_name"));
                        app.setCostCenter(app.getCostCenter() + "," + rs.getString("c_cost_center"));

                    } else {
                        app.setUsername(rs.getString("c_username"));
                        app.setClassCd(rs.getString("c_class_cd"));
                        app.setNoreg(rs.getString("c_noreg"));
                        app.setName(rs.getString("c_name"));
                        app.setClasS(rs.getString("c_class"));
                        app.setEmail(rs.getString("c_email"));
                        app.setPositionAbbr(rs.getString("c_position_abbr"));
                        app.setLaborType(rs.getString("c_labor_type"));
                        app.setDivId(rs.getString("c_div_id"));
                        app.setDivName(rs.getString("c_div_name"));
                        app.setSectionId(rs.getString("c_section_id"));
                        app.setSectionName(rs.getString("c_section_name"));
                        app.setDeptId(rs.getString("c_dept_id"));
                        app.setDeptName(rs.getString("c_dept_name"));
                        app.setCostCenter(rs.getString("c_cost_center"));

                    }
                    i++;

                }

            }

        } catch (SQLException e) {
            LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
        }

        return app;
    }

    public Approver getUserApproverDH(String positionAbbr, Approver r) {

        Approver app = new Approver();

        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        StringBuilder query = new StringBuilder();
        query
                .append("SELECT ")
                .append("a.c_username, ")
                .append("a.c_class_cd, ")
                .append("a.c_noreg, ")
                .append("a.c_name, ")
                .append("a.c_class, ")
                .append("a.c_email, ")
                .append("a.c_position_abbr, ")
                .append("a.c_labor_type, ")
                .append("a.c_div_id, ")
                .append("a.c_div_name, ")
                .append("a.c_section_id, ")
                .append("a.c_section_name, ")
                .append("a.c_dept_id, ")
                .append("a.c_dept_name, ")
                .append("a.c_cost_center ")
                .append("FROM app_fd_employee a WITH(NOLOCK) ")
                .append("WHERE a.c_position_abbr=? ")
                .append("AND a.c_div_id=? ");

        try (
                Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setString(1, positionAbbr);
            ps.setString(2, r.getDivId());

            try (ResultSet rs = ps.executeQuery()) {

                int i = 0;
                while (rs.next()) {

                    if (i > 0) {
                        app.setUsername(app.getUsername() + "," + rs.getString("c_username"));
                        app.setClassCd(app.getClassCd() + "," + rs.getString("c_class_cd"));
                        app.setNoreg(app.getNoreg() + "," + rs.getString("c_noreg"));
                        app.setName(app.getName() + "," + rs.getString("c_name"));
                        app.setClasS(app.getClasS() + "," + rs.getString("c_class"));
                        app.setEmail(app.getEmail() + "," + rs.getString("c_email"));
                        app.setPositionAbbr(app.getPositionAbbr());
                        app.setLaborType(app.getLaborType() + "," + rs.getString("c_labor_type"));
                        app.setDivId(app.getDivId() + "," + rs.getString("c_div_id"));
                        app.setDivName(app.getDivName() + "," + rs.getString("c_div_name"));
                        app.setSectionId(app.getSectionId() + "," + rs.getString("c_section_id"));
                        app.setSectionName(app.getSectionName() + "," + rs.getString("c_section_name"));
                        app.setDeptId(app.getDeptId() + "," + rs.getString("c_dept_id"));
                        app.setDeptName(app.getDeptName() + "," + rs.getString("c_dept_name"));
                        app.setCostCenter(app.getCostCenter() + "," + rs.getString("c_cost_center"));

                    } else {
                        app.setUsername(rs.getString("c_username"));
                        app.setClassCd(rs.getString("c_class_cd"));
                        app.setNoreg(rs.getString("c_noreg"));
                        app.setName(rs.getString("c_name"));
                        app.setClasS(rs.getString("c_class"));
                        app.setEmail(rs.getString("c_email"));
                        app.setPositionAbbr(rs.getString("c_position_abbr"));
                        app.setLaborType(rs.getString("c_labor_type"));
                        app.setDivId(rs.getString("c_div_id"));
                        app.setDivName(rs.getString("c_div_name"));
                        app.setSectionId(rs.getString("c_section_id"));
                        app.setSectionName(rs.getString("c_section_name"));
                        app.setDeptId(rs.getString("c_dept_id"));
                        app.setDeptName(rs.getString("c_dept_name"));
                        app.setCostCenter(rs.getString("c_cost_center"));

                    }
                    i++;

                }

            }

        } catch (SQLException e) {
            LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
        }

        return app;
    }

    public Approver getEmployeeFromMappingMaster(String type, String KeyName) {

        Approver app = new Approver();
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        StringBuilder query = new StringBuilder();

        query
                .append(" SELECT ")
                .append(" DISTINCT(a.c_username) AS c_username, ")
                .append(" a.c_class_cd, ")
                .append(" a.c_noreg, ")
                .append(" a.c_name, ")
                .append(" a.c_class, ")
                .append(" a.c_email, ")
                .append(" a.c_position_abbr, ")
                .append(" a.c_labor_type, ")
                .append(" a.c_div_id, ")
                .append(" a.c_div_name, ")
                .append(" a.c_section_id, ")
                .append(" a.c_section_name, ")
                .append(" a.c_dept_id, ")
                .append(" a.c_dept_name, ")
                .append(" a.c_cost_center, ")
                .append(" a.c_ext, ")
                .append(" b.c_type, ")
                .append(" b.c_key_name ")
                .append(" FROM app_fd_employee a WITH (NOLOCK) ")
                .append(" JOIN app_fd_am_mapping_master b WITH (NOLOCK) ON (b.c_noreg=a.c_noreg) ")
                .append(" WHERE b.c_type=? ")
                .append(" AND b.c_key_name=? ");

        try (
                Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setString(1, type);
            ps.setString(2, KeyName);

            try (ResultSet rs = ps.executeQuery()) {

                int i = 0;

                while (rs.next()) {

                    if (i > 0) {

                        if (!app.getNoreg().contains(rs.getString("c_noreg"))) {
                            app.setUsername(app.getUsername() + "," + rs.getString("c_username"));
                            app.setClassCd(app.getClassCd() + "," + rs.getString("c_class_cd"));
                            app.setNoreg(app.getNoreg() + "," + rs.getString("c_noreg"));
                            app.setName(app.getName() + "," + rs.getString("c_name"));
                            app.setClasS(app.getClasS() + "," + rs.getString("c_class"));
                            app.setEmail(app.getEmail() + "," + rs.getString("c_email"));
                            app.setPositionAbbr(app.getPositionAbbr() + "," + rs.getString("c_position_abbr"));
                            app.setLaborType(app.getLaborType() + "," + rs.getString("c_labor_type"));
                            app.setDivId(app.getDivId() + "," + rs.getString("c_div_id"));
                            app.setDivName(app.getDivName() + "," + rs.getString("c_div_name"));
                            app.setSectionId(app.getSectionId() + "," + rs.getString("c_section_id"));
                            app.setSectionName(app.getSectionName() + "," + rs.getString("c_section_name"));
                            app.setDeptId(app.getDeptId() + "," + rs.getString("c_dept_id"));
                            app.setDeptName(app.getDeptName() + "," + rs.getString("c_dept_name"));
                            app.setCostCenter(app.getCostCenter() + "," + rs.getString("c_cost_center"));
                            app.setExt(app.getExt() + "," + rs.getString("c_ext"));
                        }
                    } else {
                        app.setUsername(rs.getString("c_username"));
                        app.setClassCd(rs.getString("c_class_cd"));
                        app.setNoreg(rs.getString("c_noreg"));
                        app.setName(rs.getString("c_name"));
                        app.setClasS(rs.getString("c_class"));
                        app.setEmail(rs.getString("c_email"));
                        app.setPositionAbbr(rs.getString("c_position_abbr"));
                        app.setLaborType(rs.getString("c_labor_type"));
                        app.setDivId(rs.getString("c_div_id"));
                        app.setDivName(rs.getString("c_div_name"));
                        app.setSectionId(rs.getString("c_section_id"));
                        app.setSectionName(rs.getString("c_section_name"));
                        app.setDeptId(rs.getString("c_dept_id"));
                        app.setDeptName(rs.getString("c_dept_name"));
                        app.setCostCenter(rs.getString("c_cost_center"));
                        app.setExt(rs.getString("c_ext"));
                    }
                    i++;

                }

            }

        } catch (SQLException e) {
            LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
        }

        return app;
    }

    public String getTtd(String noreg) {

        String ttd = "";

        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        StringBuilder query = new StringBuilder();
        query
                .append(" SELECT TOP(1) ")
                .append(" id, ")
                .append(" c_image_signature ")
                .append(" FROM app_fd_er_master_ttd WITH (NOLOCK) ")
                .append(" WHERE id=? ");

        try (
                Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setString(1, noreg);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    ttd = rs.getString("c_image_signature");
                }

            }

        } catch (SQLException e) {
            LogUtil.error(this.getClass().getName(), e, "Error : " + e.getMessage());
        }

        return ttd;
    }

    public int positionInNumber(String positionAbbr) {
        int value = 0;

        if ("DH".equalsIgnoreCase(positionAbbr)) {
            value = 10;
        } else if ("DDH-P".equalsIgnoreCase(positionAbbr)) {
            value = 9;
        } else if ("DDH".equalsIgnoreCase(positionAbbr)) {
            value = 8;
        } else if ("DPH".equalsIgnoreCase(positionAbbr)) {
            value = 7;
        } else if ("PM".equalsIgnoreCase(positionAbbr)) {
            value = 6;
        } else if ("SO".equalsIgnoreCase(positionAbbr)) {
            value = 5;
        } else if ("SH".equalsIgnoreCase(positionAbbr)) {
            value = 4;
        } else if ("STAFF".equalsIgnoreCase(positionAbbr)) {
            value = 3;
        } else if ("LH".equalsIgnoreCase(positionAbbr)) {
            value = 3;
        } else if ("GH".equalsIgnoreCase(positionAbbr)) {
            value = 2;
        } else if ("OPR".equalsIgnoreCase(positionAbbr)) {
            value = 1;
        }

        return value;
    }

}
