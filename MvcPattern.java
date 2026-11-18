
public class MvcPattern {
    public static void main(String[] args) {
        // 1. Crear el Modelo (Datos iniciales)
        StudentModel model = new StudentModel("Carlos Santana", "carlos.santana@example.com");

        // 2. Crear la Vista (Mecanismo de presentación)
        StudentView view = new StudentView();

        // 3. Crear el Controlador (Intermediario que une Modelo y Vista)
        StudentController controller = new StudentController(model, view);

        // --- Simulación de interacción ---
        System.out.println("Estado inicial cargado por el controlador:");
        controller.actualizarVista();

        // El usuario (o la interfaz) cambia los datos a través del controlador
        System.out.println("El usuario actualiza los datos...");
        controller.setStudentName("María Rodríguez");
        controller.setStudentEmail("maria.rodriguez@example.com");

        // Refrescar la vista para ver los cambios
        controller.actualizarVista();
    }
}

/**
 * ==========================================
 * 1. EL MODELO (Model)
 * ==========================================
 * Contiene exclusivamente los datos y la lógica de estado.
 * No sabe nada de la Vista ni del Controlador.
 */
class StudentModel {
    private String name;
    private String email;

    public StudentModel(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

/**
 * ==========================================
 * 2. LA VISTA (View)
 * ==========================================
 * Se encarga únicamente de mostrar la información al usuario.
 * No modifica los datos ni contiene lógica de negocio.
 */
class StudentView {
    public void printStudentDetails(String studentName, String studentEmail) {
        System.out.println("========================================");
        System.out.println("         VISTA DE ESTUDIANTE            ");
        System.out.println("========================================");
        System.out.println(" Nombre : " + studentName);
        System.out.println(" Correo : " + studentEmail);
        System.out.println("========================================\n");
    }
}

/**
 * ==========================================
 * 3. EL CONTROLADOR (Controller)
 * ==========================================
 * Actúa como puente. Recibe las acciones (o actualiza el modelo)
 * y le ordena a la Vista que se refresque con los nuevos datos.
 */
class StudentController {
    private final StudentModel model;
    private final StudentView view;

    public StudentController(StudentModel model, StudentView view) {
        this.model = model;
        this.view = view;
    }

    public void setStudentName(String name) {
        model.setName(name); // Modifica el modelo
    }

    public void setStudentEmail(String email) {
        model.setEmail(email); // Modifica el modelo
    }

    public String getStudentName() {
        return model.getName();
    }

    public String getStudentEmail() {
        return model.getEmail();
    }

    // Método que coordina la actualización de la interfaz/vista
    public void actualizarVista() {
        view.printStudentDetails(model.getName(), model.getEmail());
    }
}