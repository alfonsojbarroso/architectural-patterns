import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoPattern {
    public static void main(String[] args) {
        // Ejemplo de uso conceptual (requeriría un DataSource configurado, ej. H2 o HikariCP)
        System.out.println("Estructura DAO unificada lista para usar con un DataSource.");
    }
}

/**
 * 1. La Entidad (Modelo de Datos)
 */
record StudentDto(Long id, String name, String email) {}

/**
 * 2. La Interfaz DAO (El Contrato)
 */
interface StudentDao {
    Optional<StudentDto> findById(Long id);
    List<StudentDto> findAll();
    void save(StudentDto student);
    void update(StudentDto student);
    void delete(Long id);
}

/**
 * 3. La Implementación Concreta (JDBC)
 */
class StudentDaoJdbc implements StudentDao {

    private final DataSource dataSource;

    public StudentDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<StudentDto> findById(Long id) {
        String sql = "SELECT id, name, email FROM students WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    StudentDto student = new StudentDto(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")
                    );
                    return Optional.of(student);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar estudiante por ID", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<StudentDto> findAll() {
        String sql = "SELECT id, name, email FROM students";
        List<StudentDto> students = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(new StudentDto(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los estudiantes", e);
        }

        return students;
    }

    @Override
    public void save(StudentDto student) {
        String sql = "INSERT INTO students (name, email) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.name());
            stmt.setString(2, student.email());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el estudiante", e);
        }
    }

    @Override
    public void update(StudentDto student) {
        String sql = "UPDATE students SET name = ?, email = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.name());
            stmt.setString(2, student.email());
            stmt.setLong(3, student.id());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el estudiante", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el estudiante", e);
        }
    }
}

/**
 * 4. La Capa de Negocio (Service)
 */
class StudentService {
    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void registrarNuevoEstudiante(String name, String email) {
        StudentDto nuevo = new StudentDto(null, name, email);
        studentDao.save(nuevo);
    }

    public List<StudentDto> obtenerTodosLosEstudiantes() {
        return studentDao.findAll();
    }
}