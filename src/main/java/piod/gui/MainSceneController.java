package piod.gui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.tools.StringUtils;
import piod.service.PersistenceManager;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainSceneController {

    private static Log LOGGER = LogFactory.getLog(MainSceneController.class);

    @FXML
    private GridPane tableNamesGridPane;

    @FXML
    private Label filePathLabel;
    @FXML
    private Button generateJSONButton;

    @FXML
    public void initialize() {
        List<String> tableNames = SceneDataHolder.getInstance().getTableNames();

        for (int i = 0; i < tableNames.size(); i++) {
            String singleTableName = tableNames.get(i);
            CheckBox checkBox = new CheckBox(singleTableName);
            checkBox.setPadding(new Insets(15, 0, 0, 0));
            tableNamesGridPane.addRow(i, checkBox);
        }

        generateJSONButton.setOnMousePressed(event -> fetchAllContentsFromSelectedTables());
    }

    private List<String> getSelectedTableNames() {
        List<String> selectedTableNames = new LinkedList<>();

        for (Node node : tableNamesGridPane.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    selectedTableNames.add(checkBox.getText());
                }
            }
        }
        return selectedTableNames;
    }

    public void fetchAllContentsFromSelectedTables() {
        PersistenceManager persistenceManager = new PersistenceManager();
        List<String> selectedTableNames = getSelectedTableNames();
        List<Result<Record>> results = persistenceManager.fetchAllContentsFromTables(selectedTableNames, SceneDataHolder.getInstance().getConnectionManager());

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < results.size(); i++) {
            Result<Record> singleResult = results.get(i);
            stringBuilder.append("--------------------------------------------------------------------------").append("\n");
            stringBuilder.append("Contents of table \"").append(selectedTableNames.get(i)).append("\":\n");
            for (Record record : singleResult) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.createObjectNode();

                Field<?>[] fields = record.fields();

                for (Field<?> field : fields) {
                    String name = field.getName();
                    Object value = record.getValue(name);
                    ((ObjectNode) rootNode).put(name, value.toString());
                }
                stringBuilder.append(rootNode.toString()).append("\n");
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date date = new Date();

        String filePath = "jsonResults/" + dateFormat.format(date) + ".txt";
        if (!StringUtils.isEmpty(stringBuilder.toString())) {
            try (PrintWriter out = new PrintWriter(filePath)) {
                out.println(stringBuilder.toString());
                filePathLabel.setText("Export successful.\nJSON file path: " + filePath);
            } catch (FileNotFoundException e) {
                LOGGER.error(e);
            }
        } else {
            filePathLabel.setText("Export failed.");
        }
    }
}