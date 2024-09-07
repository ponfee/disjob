/*
 * Copyright 2022-2024 Ponfee (http://www.ponfee.cn/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ponfee.disjob.common.dag;

import cn.ponfee.disjob.common.util.Files;
import cn.ponfee.disjob.common.util.MavenProjects;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * DAG printer
 *
 * @author Ponfee
 */
public class DAGPrinter {

    public static void main(String[] args) throws Exception {
        drawGraph("A", "dag0.png");
        drawGraph("A -> B,C,D", "dag1.png");
        drawGraph("A,B,C -> D", "dag2.png");
        drawGraph("A -> B,C,D -> E", "dag3.png");
        drawGraph("A -> B,C -> E,(F->G) -> H", "dag4.png");
        drawGraph("A -> (B->C->D),(A->F) -> G,H,X -> J ; A->Y", "dag5.png");
        drawGraph("ALoader -> (BMap->CMap->DMap),(AMap->FMap) -> GShuffle,HShuffle,XShuffle -> JReduce ; A->Y", "dag6.png");
        drawGraph("A->B,C,(D->E)->D,F->G", "dag7.png");

        drawGraph("A->B,C,D",                 "10.png");
        drawGraph("A->B->C,D",                "20.png");
        drawGraph("A->B->C->D->G;A->E->F->G", "30.png");
        drawGraph("A->(B->C->D),(E->F)->G",   "31.png");
        drawGraph("A->B->C,D,E;A->H->I,J,K",  "40.png");
        drawGraph("A->(B->C,D,E),(H->I,J,K)", "41.png");
        drawGraph("A,B,C->D",                 "50.png");

        drawGraph(
            "[                                                \n" +
            "  {\"source\": \"1:1:A\", \"target\": \"1:1:C\"},\n" +
            "  {\"source\": \"1:1:A\", \"target\": \"1:1:D\"},\n" +
            "  {\"source\": \"1:1:B\", \"target\": \"1:1:D\"},\n" +
            "  {\"source\": \"1:1:B\", \"target\": \"1:1:E\"} \n" +
            "]                                                  ",
            "json-graph.png"
        );
    }

    private static void drawGraph(String expr, String fileName) throws IOException {
        File file = new File(MavenProjects.getProjectBaseDir() + "/target/dag/" + fileName);
        FileUtils.deleteQuietly(file);
        Files.mkdirIfNotExists(file.getParentFile());
        DAGUtils.drawPngImage(expr, "Example", 2000, new FileOutputStream(file));
    }

}
