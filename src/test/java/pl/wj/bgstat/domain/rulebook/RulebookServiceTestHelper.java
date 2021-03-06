package pl.wj.bgstat.domain.rulebook;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RulebookServiceTestHelper {

    private static final String OK_FILE_NAME = "SampleRulebook.pdf";
    private static final String OK_FILE_PATH = "src/test/resources/" + OK_FILE_NAME;
    private static final String NOT_OK_FILE_NAME = "notOkFile.png";
    private static final String NOT_OK_FILE_PATH = "src/test/resources/" + NOT_OK_FILE_NAME;

    public static InputStream createInputStreamOfRulebook(boolean correctMediaType) throws IOException {
        return createMultipartRulebook(correctMediaType).getInputStream();
    }

    private static MultipartFile createMultipartRulebook(boolean correctMediaType) {
        MultipartFile mpf = null;

        try {
            if(correctMediaType) {
                File file = new File(OK_FILE_PATH);
                InputStream is = new FileInputStream(file);
                mpf = new MockMultipartFile(OK_FILE_NAME, OK_FILE_NAME, MediaType.APPLICATION_PDF_VALUE, is);
            } else {
                File file = new File(NOT_OK_FILE_PATH);
                InputStream is = new FileInputStream(file);
                mpf = new MockMultipartFile(NOT_OK_FILE_NAME, NOT_OK_FILE_NAME, MediaType.APPLICATION_PDF_VALUE, is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return mpf;
        }
    }

}
