package merikbest.waterpipelinesystem.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import merikbest.waterpipelinesystem.domain.Point;
import merikbest.waterpipelinesystem.domain.Result;
import merikbest.waterpipelinesystem.domain.WaterPipeline;
import merikbest.waterpipelinesystem.repository.PointRepository;
import merikbest.waterpipelinesystem.repository.ResultRepository;
import merikbest.waterpipelinesystem.repository.WaterPipelineRepository;
import merikbest.waterpipelinesystem.service.GraphWeighted;
import merikbest.waterpipelinesystem.service.NodeWeighted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {

    private final WaterPipelineRepository waterPipelineRepository;
    private final PointRepository pointRepository;
    private final ResultRepository resultRepository;

    @Autowired
    public MainController(WaterPipelineRepository waterPipelineRepository,
                          PointRepository pointRepository,
                          ResultRepository resultRepository) {
        this.waterPipelineRepository = waterPipelineRepository;
        this.pointRepository = pointRepository;
        this.resultRepository = resultRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/result")
    public String uploadCSVFile(
            @RequestParam("file1") MultipartFile file1,
            @RequestParam("file2") MultipartFile file2,
            Model model
    ) {

        try (Reader reader = new BufferedReader(new InputStreamReader(file1.getInputStream()))) {

            CsvToBean<WaterPipeline> csvToBean = new CsvToBeanBuilder(reader)
                    .withSeparator(';')
                    .withType(WaterPipeline.class)
                    .build();

            List<WaterPipeline> descriptions = csvToBean.parse();

            waterPipelineRepository.saveAll(descriptions);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Reader reader = new BufferedReader(new InputStreamReader(file2.getInputStream()))) {

            CsvToBean<Point> csvToBean = new CsvToBeanBuilder(reader)
                    .withSeparator(';')
                    .withType(Point.class)
                    .build();

            List<Point> points = csvToBean.parse();

            pointRepository.saveAll(points);

        } catch (IOException e) {
            e.printStackTrace();
        }

        GraphWeighted graphWeighted = new GraphWeighted(true);
        Set<Integer> nodeNames = new HashSet<>();
        List<NodeWeighted> nodeWeighteds = new ArrayList<>();

        List<WaterPipeline> pipelines = waterPipelineRepository.findAll();
        List<Point> points = pointRepository.findAll();


        for (WaterPipeline pipeline : pipelines) {
            nodeNames.add(pipeline.getIdX());
            nodeNames.add(pipeline.getIdY());
        }

        for (Integer nodeName : nodeNames) {
            nodeWeighteds.add(new NodeWeighted(nodeName, nodeName.toString()));
        }

        for (WaterPipeline pipeline : pipelines) {
            for (NodeWeighted nodeWeighted1 : nodeWeighteds) {
                if (nodeWeighted1.getN() == pipeline.getIdX()) {
                    for (NodeWeighted nodeWeighted2 : nodeWeighteds) {
                        if (nodeWeighted2.getN() == pipeline.getIdY()) {
                            graphWeighted.addEdge(nodeWeighted1, nodeWeighted2, pipeline.getLength());
                        }
                    }
                }
            }
        }

        for (Point point : points) {
            graphWeighted.DijkstraShortestPath(nodeWeighteds.get(point.getIdA() - 1), nodeWeighteds.get(point.getIdB() - 1));
        }

        for (String resultString : graphWeighted.getResult()) {
            System.out.println(resultString);

            if (resultString.startsWith("FALSE")) {
                Result result = new Result();
                result.setRouteExists("FALSE");
                result.setMinLength("");
                resultRepository.save(result);
            }

            if (resultString.startsWith("TRUE")) {
                Result result = new Result();
                String[] splitedResultString = resultString.split("\\s+");
                result.setRouteExists(splitedResultString[0]);
                result.setMinLength(splitedResultString[1]);
                resultRepository.save(result);
            }
        }

        model.addAttribute("results", resultRepository.findAll());

        return "result";
    }

    @GetMapping("/result")
    public void exportCSV(HttpServletResponse response) throws Exception {
        String filename = "result.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        StatefulBeanToCsv<Result> writer = new StatefulBeanToCsvBuilder<Result>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(';')
                .withOrderedResults(false)
                .build();

        writer.write(resultRepository.findAll());
    }
}
