package acceptancetests._02databasestubpriming.testinfrastructure.renderers;

import acceptancetests._02databasestubpriming.testinfrastructure.stub.SpeciesInfoRecord;
import com.googlecode.yatspec.rendering.Renderer;

import java.util.Objects;

public class SpeciesInfoInDatabaseRendererVersion2 implements Renderer<SpeciesInfoRecord> {

    private static final int NUMBER_OF_EXPECTED_FIELDS = 6;

    @Override
    public String render(SpeciesInfoRecord speciesInfoRecord) {
        // We want to render what is being put in the database
        if (Objects.isNull(speciesInfoRecord.getSpeciesInfoId())) {
            return speciesInfoRecord.toString();
        }
        // This renders what is in the database (either before or after when has occurred)
        StringBuilder tableHtml = new StringBuilder().append("<table>");

        addHeaders(tableHtml);

        tableHtml.append("<tr>");
        addCellFor(speciesInfoRecord, tableHtml);
        tableHtml.append("</tr>");

        return tableHtml.append("</table>").toString();
    }

    private void addHeaders(StringBuilder tableHtml) {
        tableHtml.append("<tr>");
        tableHtml.append("<th> id </th>");
        tableHtml.append("<th> speciesInfoId </th>");
        tableHtml.append("<th> personId </th>");
        tableHtml.append("<th> species </th>");
        tableHtml.append("<th> averageHeight </th>");
        tableHtml.append("<th> lifespan </th>");
        tableHtml.append("</tr>");
    }

    public void addCellFor(SpeciesInfoRecord speciesInfoRecord, StringBuilder cell) {
        final int numberOfFields = SpeciesInfoRecord.class.getDeclaredFields().length;
        if (numberOfFields != NUMBER_OF_EXPECTED_FIELDS){ // A check to make sure that object used in rendering has same fields as those that make the generated table
            throw new IllegalStateException(String.format("Yatspec renderer is missing getter, should have %s but found %s", NUMBER_OF_EXPECTED_FIELDS, numberOfFields));
        }
        cell.append("<td >").append(speciesInfoRecord.getId()).append("</td>");
        cell.append("<td >").append(speciesInfoRecord.getSpeciesInfoId()).append("</td>");
        cell.append("<td >").append(speciesInfoRecord.getPersonId()).append("</td>");
        cell.append("<td >").append(speciesInfoRecord.getName()).append("</td>");
        cell.append("<td >").append(speciesInfoRecord.getAverageHeight()).append("</td>");
        cell.append("<td >").append(speciesInfoRecord.getLifespan()).append("</td>");
    }
}
