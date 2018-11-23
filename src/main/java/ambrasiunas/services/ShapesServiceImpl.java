/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.services;

import ambrasiunas.Application;
import ambrasiunas.domain.EmbeddedRepository;
import ambrasiunas.domain.ShapesRepository;
import ambrasiunas.domain.shapes.AnyShape;
import ambrasiunas.domain.shapes.PlainCoordinate;
import ambrasiunas.domain.shapes.ShapeDTO;
import ambrasiunas.exception.InvalidShapeParameterInputException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ShapesServiceImpl implements ShapeService {

    private ShapesRepository shapesRepository;
    private CalculationService calculationService;
    private ObjectMapper objectMapper;
    private ShapeFactory shapeFactory;

    public ShapesServiceImpl() {
        this.shapesRepository = new EmbeddedRepository();
        this.calculationService = new ShapePropertiesCalculator();
        this.shapeFactory = new ShapeFactory();
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
    }

    @Override
    public AnyShape createAndSaveShape(AnyShape anyShape) {
        Integer shapeId = 0;
        shapeId = shapesRepository.saveShape(anyShape, "User");
        anyShape.setId(shapeId);
        return anyShape;
    }

    @Override
    public List<AnyShape> getAllShapes() {
        return shapesRepository.getPaginatedShapes();
    }

    @Override //Todo Implement ShapeCalculation service
    public List<AnyShape> getShapesInCoordinate(PlainCoordinate plainCoordinate) {
        List<AnyShape> allAnyShapes = shapesRepository.getPaginatedShapes();
        List<AnyShape> shapesWithCoordinate = new ArrayList<>();
        if (allAnyShapes.size() != 0) {
            for (AnyShape anyShape : allAnyShapes) {
                if (anyShape.isInArea(plainCoordinate)) {
                    shapesWithCoordinate.add(anyShape);
                }
            }
        }

        return shapesWithCoordinate;
    }

    @Override
    public void loadShapeFromFile() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Runnable task = new Runnable() {
            public void run() {
                List<ShapeDTO> shapeDTOS = null;
                System.out.print("Loading shapes from file.....\n");
                try {
                    InputStream fileStream = Application.class.getClassLoader().getResourceAsStream("shapes.json");
                    shapeDTOS = objectMapper.readValue(fileStream, new TypeReference<List<ShapeDTO>>() {
                    });
                    for (ShapeDTO shapeDTO : shapeDTOS) {
                        if (shapeDTO.getType() != null && shapeDTO.getType() != null) {
                            AnyShape anyShapeFromFile = shapeFactory.getShape(shapeDTO.getType(), shapeDTO.getParameters());
                            shapesRepository.saveShape(anyShapeFromFile, "file");
                        }
                    }
                    System.out.print("Loading done....\n");
                } catch (JsonMappingException e) {
                    System.out.print("Mapping error: " + e.getMessage());
                } catch (JsonParseException e) {
                    System.out.print("Parsing error: " + e.getMessage());
                } catch (IOException e) {
                    System.out.print("IOexception error: " + e.getMessage());
                } catch (InvalidShapeParameterInputException ex) {
                    System.out.print(ex.getMessage() + "\n");
                }
            }
        };
        try {
            executor.submit(task);
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.print("error" + e);
        }
    }
}
