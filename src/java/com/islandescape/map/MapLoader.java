package com.islandescape.map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;
import java.util.zip.Inflater;

/*
    MapLoader reads a Tiled.tmx file and builds a TileMap
    Supports base64 + zlib compressed tile layers
 */
public class MapLoader {

    public static TileMap load(String path) throws Exception {
        //Parse the XML file
        File file = new File(path);
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();

        // Read map attributes
        Element mapElement = doc.getDocumentElement();
        int width    = Integer.parseInt(mapElement.getAttribute("width"));
        int height   = Integer.parseInt(mapElement.getAttribute("height"));
        int tileSize = Integer.parseInt(mapElement.getAttribute("tilewidth"));

        TileMap tileMap = new TileMap(width, height, tileSize);

        // Read each tile layer
        NodeList layerNodes = doc.getElementsByTagName("layer");
        for (int i = 0; i < layerNodes.getLength(); i++) {
            Element layerElement = (Element) layerNodes.item(i);
            String layerName = layerElement.getAttribute("name");

            //Get the layer data
            Element dataElement = (Element) layerElement
                    .getElementsByTagName("data").item(0);

            String encoding = dataElement.getAttribute("encoding");
            int[][] tileData;
            if ("csv".equals(encoding)) {
                tileData = parseCsvLayerData(dataElement.getTextContent().trim(), width, height);
            } else {
                tileData = decompressLayerData(dataElement.getTextContent().trim(), width, height);
            }
            tileMap.addLayer(new TileLayer(layerName, tileData));
        }

        return tileMap;
    }

    // Parses CSV encoded layer data into a 2D int array [row][col]
    // Preserves full raw value including flip/rotation flags in upper bits
    private static int[][] parseCsvLayerData(String csvData, int width, int height) {
        int[][] data = new int[height][width];
        String[] values = csvData.split(",");
        int index = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                data[row][col] = (int) Long.parseLong(values[index].trim());
                index++;
            }
        }
        return data;
    }

    // Decodes base64 + zlib compressed layer data into a 2D int array [row][col]
    private static int[][] decompressLayerData(String base64Data, int width, int height) throws Exception {
        byte[] compressed   = Base64.getDecoder().decode(base64Data);
        byte[] decompressed = new byte[width * height * 4]; // each tile = 4 bytes

        Inflater inflater = new Inflater();
        inflater.setInput(compressed);
        inflater.inflate(decompressed);
        inflater.end();

        int[][] data = new int[height][width];
        ByteBuffer buffer = ByteBuffer.wrap(decompressed).order(ByteOrder.LITTLE_ENDIAN);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // Preserve full raw value including flip/rotation flags
                data[row][col] = buffer.getInt();
            }
        }

        return data;
    }
}
