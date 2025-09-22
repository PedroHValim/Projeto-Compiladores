import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class lerArquivo {

    public static String lerArquivoComLinha(String caminhoDoArquivo) throws IOException {
        return Files.lines(Paths.get(caminhoDoArquivo)).collect(Collectors.joining("\n"));
    }
    
}