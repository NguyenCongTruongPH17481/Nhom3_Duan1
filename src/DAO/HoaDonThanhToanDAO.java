package DAO;

import model.HoaDonThanhToan;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HoaDonThanhToanDAO extends ShoesSysDAO<HoaDonThanhToan, String> {

    String SQL_Insert = "INSERT INTO dbo.HoaDonThanhToan (MaHDThanhToan, MaKhachHang, MaNhanVien, NgayThanhToan, DiemThuong, DoiDiem, GhiChu, TrangThai)  VALUES (?,?,?,?,?,?,?,1)";
    String SQL_Update = "UPDATE dbo.HoaDonThanhToan SET MaKhachHang=?, MaNhanVien=?, NgayThanhToan=?, DiemThuong=?, GhiChu=? WHERE MaHDThanhToan=?";
    String SQL_VoHieuHoa = "UPDATE dbo.HoaDonThanhToan SET TrangThai=0 WHERE MaHDThanhToan = ?";
    String SQL_SelectALL = "SELECT * FROM dbo.HoaDonThanhToan";
    String SQL_SelectID = "SELECT * FROM dbo.HoaDonThanhToan WHERE MaHDThanhToan=?";

    @Override
    public void insert(HoaDonThanhToan entity) {
        try {
            helper.JdbcHelper.update(SQL_Insert,
                    entity.getMaHDThanhToan(), entity.getMaKH(), entity.getMaNV(), entity.getNgayThanhToan(),
                    entity.getDiemThuong(), entity.getDoiDiem(), entity.getGhiChu());
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonThanhToanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(HoaDonThanhToan entity) {
        try {
            helper.JdbcHelper.update(SQL_Update,
                    entity.getMaKH(), entity.getMaNV(), entity.getNgayThanhToan(), entity.getDiemThuong(),
                    entity.getGhiChu(), entity.getMaHDThanhToan());
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonThanhToanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void vohieuhoa(String id) {
        try {
            helper.JdbcHelper.update(SQL_VoHieuHoa, id);
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonThanhToanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<HoaDonThanhToan> selectAll() {
        return this.selectBySql(SQL_SelectALL);
    }

    @Override
    public HoaDonThanhToan selectById(String id) {
        List<HoaDonThanhToan> list = this.selectBySql(SQL_SelectID, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    protected List<HoaDonThanhToan> selectBySql(String sql, Object... args) {
        List<HoaDonThanhToan> list = new ArrayList<>();
        try {
            ResultSet rs = helper.JdbcHelper.query(sql, args);
            while (rs.next()) {
                HoaDonThanhToan entity = new HoaDonThanhToan();
                entity.setMaHDThanhToan(rs.getString("MaHDThanhToan"));
                entity.setMaKH(rs.getString("MaKhachHang"));
                entity.setMaNV(rs.getString("MaNhanVien"));
                entity.setNgayThanhToan(rs.getString("NgayThanhToan"));
                entity.setDiemThuong(rs.getInt("DiemThuong"));
                entity.setDoiDiem(rs.getInt("DoiDiem"));
                entity.setGhiChu(rs.getString("GhiChu"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<HoaDonThanhToan> selectPage(String keyword, String ngaybd, String ngaykt, int index) {
        String sql = "SELECT * FROM dbo.HoaDonThanhToan\n"
                + "WHERE MaHDThanhToan LIKE ? AND TrangThai=1\n"
                + "AND NgayThanhToan BETWEEN ? AND ?\n"
                + "ORDER BY MaHDThanhToan OFFSET ? * 15 ROWS FETCH NEXT 15 ROWS ONLY;";
        return this.selectBySql(sql, "%" + keyword + "%", ngaybd, ngaykt, index);
    }
    
    public HoaDonThanhToan selectThongKe(String ngay) {
        List<HoaDonThanhToan> list = this.selectBySql("SELECT * FROM dbo.HoaDonThanhToan WHERE NgayThanhToan = ?", ngay);
        return list.isEmpty() ? null : list.get(0);
    }
}
