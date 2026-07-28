/**
 * Copyright (c) 2013 Oculus Info Inc. http://www.oculusinfo.com/
 *
 * <p>Released under the MIT License.
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.oculusinfo.ml.unsupervised;

import com.oculusinfo.ml.DataSet;
import com.oculusinfo.ml.Instance;
import com.oculusinfo.ml.feature.numeric.NumericVectorFeature;
import com.oculusinfo.ml.feature.numeric.centroid.MeanNumericVectorCentroid;
import com.oculusinfo.ml.feature.numeric.distance.EuclideanDistance;
import com.oculusinfo.ml.unsupervised.cluster.Cluster;
import com.oculusinfo.ml.unsupervised.cluster.ClusterResult;
import com.oculusinfo.ml.unsupervised.cluster.kmeans.KMeans;

/**
 * Hello-world exercise of Influent's core "dynamic entity clustering" engine (the
 * ensemble-clustering library). Groups financial actors by a 2D behavioural feature vector [avg
 * transaction amount, monthly activity] — the kind of summarization Influent uses at scale.
 *
 * <p>Run from the repo root after a build (see AGENTS.md for the exact classpath command).
 */
public class EntityClusteringDemo {
  private static final String FEATURE = "behaviour";

  public static void main(String[] args) {
    System.out.println("=== Influent entity-clustering hello world ===");

    // Sample entities: name -> [avg transaction amount, monthly activity]
    String[] names = {
      "acct:alice", "acct:bob", "acct:carol", // small retail actors
      "acct:megacorp", "acct:globalbank", "acct:fund", // large institutional actors
      "acct:mule1", "acct:mule2" // mid-size suspicious cluster
    };
    double[][] behaviour = {
      {120.0, 8.0}, {95.0, 12.0}, {150.0, 6.0},
      {90000.0, 220.0}, {88000.0, 240.0}, {91000.0, 205.0},
      {5000.0, 90.0}, {5200.0, 85.0}
    };

    DataSet ds = new DataSet();
    for (int i = 0; i < names.length; i++) {
      Instance inst = new Instance(names[i]);
      NumericVectorFeature v = new NumericVectorFeature(FEATURE);
      v.setValue(behaviour[i]);
      inst.addFeature(v);
      ds.add(inst);
    }
    System.out.println("Loaded " + ds.size() + " entities to cluster.\n");

    KMeans clusterer = new KMeans(3, 25, false);
    clusterer.registerFeatureType(
        FEATURE, MeanNumericVectorCentroid.class, new EuclideanDistance(1.0));

    ClusterResult clusters = clusterer.doCluster(ds);

    System.out.println("Clustering produced " + clusters.size() + " entity clusters:\n");
    int idx = 1;
    for (Cluster c : clusters) {
      System.out.println("  Cluster " + (idx++) + " (" + c.getMembers().size() + " members):");
      for (Instance m : c.getMembers()) {
        double[] p = ((NumericVectorFeature) m.getFeature(FEATURE)).getValue();
        System.out.printf("     - %-16s avg_amount=%9.1f  activity=%6.1f%n", m.getId(), p[0], p[1]);
      }
    }
    clusterer.terminate();
    System.out.println("\nSUCCESS: ensemble-clustering engine ran end-to-end.");
  }
}
