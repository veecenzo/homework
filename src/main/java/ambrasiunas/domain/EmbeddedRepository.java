/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.domain;

import ambrasiunas.domain.shapes.AnyShape;
import ambrasiunas.exception.InvalidShapeParameterInputException;
import ambrasiunas.services.ApplicationPropertiesService;
import ambrasiunas.services.ApplicationPropertiesServiceImpl;
import ambrasiunas.services.ShapeFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.tools.DeleteDbFiles;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmbeddedRepository implements ShapesRepository {

    ApplicationPropertiesService applicationPropertiesService;
    ShapeFactory shapeFactory;

    ObjectMapper objectMapper;

    public EmbeddedRepository() {
        applicationPropertiesService = new ApplicationPropertiesServiceImpl();
        this.objectMapper = new ObjectMapper();
        this.shapeFactory = new ShapeFactory();
        createDatabase();
    }

    @Override
    public List<AnyShape> getPaginatedShapes() {

        Long totalShapeRecords = getRecordCount();
        List<AnyShape> allShapes = null;
        if (null != totalShapeRecords) {
            Long pageSize = 10L;
            Long totalPages = totalShapeRecords / pageSize;
            if (totalPages == 0 || totalPages % totalShapeRecords != 0) {
                totalPages = totalPages + 1L;
            }
            allShapes = new ArrayList<>();
            for (Integer i = 0; i < totalPages; i++) {
                List<AnyShape> paginatedShapes = getShapesPage(pageSize, pageSize * i);
                allShapes.addAll(paginatedShapes);
            }
        }
        return allShapes;
    }

    @Override
    public Integer saveShape(AnyShape anyShape, String creatorInstance) {
        Integer shapeId = null;

        try {
            shapeId = executeOrUpdate(prepeareInsertQuery(anyShape.getShapeName(), objectMapper.writeValueAsString(anyShape.getParameters()), creatorInstance));
        } catch (Exception e) {
            System.out.println("Failed save shape: " + e.getMessage() + "\n");
        }

        return shapeId;
    }

    private List<AnyShape> getShapesPage(Long limit, Long offset) {
        List<AnyShape> anyShapes = new ArrayList<>();

        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getH2DbConnection();
            connection.setAutoCommit(false);
            preparedStatement = getPagedQuery(limit, offset);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                List<Double> parameters = objectMapper.readValue(resultSet.getString("shape_parameters"), new TypeReference<List<Double>>() {
                });
                AnyShape dbShape = shapeFactory.getShape(resultSet.getString("shape_name"), parameters);
                if (dbShape != null) {
                    dbShape.setId(resultSet.getInt("shape_id"));
                    anyShapes.add(dbShape);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to shapesPage statement: " + e.getMessage() + "\n");
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                System.out.println("Failed to close connection: " + ex.getMessage() + "\n");
            }
        }


        return anyShapes;
    }


    private Integer executeOrUpdate(PreparedStatement preparedStatement) {
        Connection connection = null;
        Integer result = null;
        try {
            connection = getH2DbConnection();
            connection.setAutoCommit(false);

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }

            preparedStatement.close();
            connection.commit();
        } catch (Exception e) {
            System.out.println("Failed to executeOrUpdate statement: " + e.getMessage() + "\n");
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                System.out.println("Failed to close connection: " + ex.getMessage() + "\n");
            }
        }

        return result;
    }

    private Long getRecordCount() {
        Connection connection = null;
        Long recordCount = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getH2DbConnection();
            connection.setAutoCommit(false);
            preparedStatement = getCountQuery();
            preparedStatement.executeQuery();

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                recordCount = resultSet.getLong(1);
            }

            preparedStatement.close();
            connection.commit();
        } catch (Exception e) {
            System.out.println("Failed to count shape records: " + e.getMessage() + "\n");
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                System.out.println("Failed to close connection: " + ex.getMessage() + "\n");
            }
        }

        return recordCount;
    }

    private void createDatabase() {
        String freshStart = applicationPropertiesService.getAppProperties().getProperty("h2.resetOnStartup");
        if ("true".equalsIgnoreCase(freshStart)) {
            DeleteDbFiles.execute(
                    applicationPropertiesService.getAppProperties().getProperty("h2.dir"),
                    applicationPropertiesService.getAppProperties().getProperty("h2.name"),
                    true
            );
        }
        Integer resultSet;
        try {
            executeOrUpdate(getCreateDatabaseQuery());
            System.out.print("Initialized database...\n");
        } catch (Exception e) {
            System.out.println("Failed create database: " + e.getLocalizedMessage() + "\n");
        }

    }

    private PreparedStatement getCountQuery() throws SQLException {
        return getH2DbConnection().prepareStatement("SELECT COUNT(*) FROM SHAPES");
    }

    private PreparedStatement getPagedQuery(Long limit, Long offset) throws SQLException {
        return getH2DbConnection().prepareStatement("SELECT * FROM SHAPES ORDER BY shape_id LIMIT " + limit + " OFFSET " + offset);
    }

    private PreparedStatement getCreateDatabaseQuery() throws SQLException {
        return getH2DbConnection().prepareStatement("CREATE TABLE IF NOT EXISTS SHAPES(shape_id BIGINT AUTO_INCREMENT PRIMARY KEY, shape_name VARCHAR(255), shape_parameters LONGVARCHAR, created_by LONGVARCHAR, created_timestamp TIMESTAMP)", Statement.RETURN_GENERATED_KEYS);
    }

    private PreparedStatement prepeareInsertQuery(String shapeName, String parameters, String creatorInstance) throws SQLException {
        PreparedStatement insertPreparedStatement = getH2DbConnection().prepareStatement("INSERT INTO SHAPES (shape_name, shape_parameters, created_by, created_timestamp) VALUES (?,?,?,CURRENT_TIMESTAMP())");
        insertPreparedStatement.setString(1, shapeName);
        insertPreparedStatement.setString(2, parameters);
        insertPreparedStatement.setString(3, creatorInstance);
        return insertPreparedStatement;
    }

    private PreparedStatement prepeareTruncateQuery() throws SQLException {
        return getH2DbConnection().prepareStatement("TRUNCATE TABLE  SHAPES;");
    }

    private Connection getH2DbConnection() throws SQLException {
        return DriverManager.getConnection(
                applicationPropertiesService.getAppProperties().getProperty("h2.url"),
                applicationPropertiesService.getAppProperties().getProperty("h2.user"),
                applicationPropertiesService.getAppProperties().getProperty("h2.pass"));
    }
}
