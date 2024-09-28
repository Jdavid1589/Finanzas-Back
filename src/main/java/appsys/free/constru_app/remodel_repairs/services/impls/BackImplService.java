package appsys.free.constru_app.remodel_repairs.services.impls;

import appsys.free.constru_app.remodel_repairs.entities.Departament;
import appsys.free.constru_app.remodel_repairs.entities.Municipality;
import appsys.free.constru_app.remodel_repairs.repositories.IMunicipalityRepo;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IBackService;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IMunicipalityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BackImplService implements IBackService {
    private static final Logger logger = LoggerFactory.getLogger(BackImplService.class);

    @Value("${spring.datasource.password}")
    private String passw;
    @Override
    public boolean createBack() {
        // variable entorno del sistema
        //Agregrar al PATH ;ruta del mysqldump
        //Crear backup
        //String comando = "mysqldump -u root -p geservip_v3 | openssl enc -aes-256-cbc -e -k Ap.**2697_> backup_encriptado.sql.enc";

        String comando = "mysqldump -u root -p"+passw+" constru_app";
        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
            String fecha=formato.format(new Date());
            //Agra
            Process child = Runtime.getRuntime().exec(comando);
            InputStream input = child.getInputStream();
            FileOutputStream output = new FileOutputStream("c:/distCA/Back/"+fecha+".sql");
            byte[] buf = new byte[1024];
            int len;
            while ((len = input.read(buf)) > 0) {
                output.write(buf, 0, len);
            }
            input.close();

            output.close();
            encript(fecha);
            return true;

        } catch (Exception ex) {
            logger.error("ERROR generar Rollback " + ex.getMessage());
            return false;
        }
    }

    private void encript(String fecha) {

        try {
            // Formatea el comando para usarlo en exec.
            String command = "openssl enc -aes-256-cbc -salt -in c:/distCA/Back/"+fecha+".sql -out c:/distCA/Back/"+fecha+".sql.enc -pass pass:Ap.2697";

            // Ejecuta el comando.
            Process process = Runtime.getRuntime().exec(command);

            // Espera a que el proceso termine.
            int exitCode = process.waitFor();

            // Verifica si el proceso terminó correctamente.
            if (exitCode == 0) {
                File archivo = new File("c:/distCA/Back/"+fecha+".sql");
                archivo.delete();

            } else {
                // Puedes optar por leer la salida de error para obtener más detalles.
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.error("No se pudo encriptar backup " + line);
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("No se pudo encriptar backup " + e.getMessage());
        }
    }
}
