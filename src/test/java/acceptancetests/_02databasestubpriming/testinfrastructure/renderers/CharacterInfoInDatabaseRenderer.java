package acceptancetests._02databasestubpriming.testinfrastructure.renderers;

import acceptancetests._02databasestubpriming.testinfrastructure.stub.CharacterInfoRecord;
import com.googlecode.yatspec.rendering.Renderer;

import java.util.Objects;

public class CharacterInfoInDatabaseRenderer implements Renderer<CharacterInfoRecord> {

    private static final int NUMBER_OF_EXPECTED_FIELDS = 5;

    @Override
    public String render(CharacterInfoRecord characterInfoRecord) throws Exception {
        // We want to render what is being put in the database
        if (Objects.isNull(characterInfoRecord.getCharacterInfoId())) {
            return characterInfoRecord.toString();
        }
        // This renders what is in the database (either before or after when has occurred)
        StringBuilder tableHtml = new StringBuilder().append("<table>");

        addHeaders(tableHtml);

        tableHtml.append("<tr>");
        addCellFor(characterInfoRecord, tableHtml);
        tableHtml.append("</tr>");

        return tableHtml.append("</table>").toString();
    }

    private void addHeaders(StringBuilder tableHtml) {
        tableHtml.append("<tr>");
        tableHtml.append("<th> id </th>");
        tableHtml.append("<th> characterInfoId </th>");
        tableHtml.append("<th> personId </th>");
        tableHtml.append("<th> birthYear </th>");
        tableHtml.append("<th> personName </th>");
        tableHtml.append("</tr>");
    }

    public void addCellFor(CharacterInfoRecord characterInfoRecord, StringBuilder cell) {
        final int numberOfFields = CharacterInfoRecord.class.getDeclaredFields().length;
        if (numberOfFields != NUMBER_OF_EXPECTED_FIELDS){ // A check to make sure that object used in rendering has same fields as those that make the generated table
            throw new IllegalStateException(String.format("Yatspec renderer is missing getter, should have %s but found %s", NUMBER_OF_EXPECTED_FIELDS, numberOfFields));
        }
        cell.append("<td >").append(characterInfoRecord.getId()).append("</td>");
        cell.append("<td >").append(characterInfoRecord.getCharacterInfoId()).append("</td>");
        cell.append("<td >").append(characterInfoRecord.getPersonId()).append("</td>");
        cell.append("<td >").append(characterInfoRecord.getBirthYear()).append("</td>");
        cell.append("<td >").append(characterInfoRecord.getPersonName()).append("</td>");
    }
}
