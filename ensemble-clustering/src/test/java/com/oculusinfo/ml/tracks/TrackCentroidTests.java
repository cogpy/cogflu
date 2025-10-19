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
package com.oculusinfo.ml.tracks;

import com.oculusinfo.geometry.geodesic.Position;
import com.oculusinfo.geometry.geodesic.PositionCalculationParameters;
import com.oculusinfo.geometry.geodesic.PositionCalculationType;
import com.oculusinfo.geometry.geodesic.Track;
import com.oculusinfo.geometry.geodesic.tracks.Cartesian3DTrack;
import com.oculusinfo.ml.feature.spatial.TrackFeature;
import com.oculusinfo.ml.feature.spatial.centroid.TrackCentroid;
import junit.framework.Assert;
import org.junit.Test;

public class TrackCentroidTests {
  private static final double EPSILON = 1E-12;
  private static final PositionCalculationParameters CARTESIAN_PARAMETERS =
      new PositionCalculationParameters(PositionCalculationType.Cartesian3D, 1E-4, EPSILON, false);

  @Test
  public void testAddRemoveTrack() {
    TrackFeature a =
        createFeature(
            new Cartesian3DTrack(
                CARTESIAN_PARAMETERS,
                new Position(0.0, 6000000.0, 0.0, false),
                new Position(6.0, 6000000.0, 0.0, false),
                new Position(6.0, 6000000.0, 6.0, false)));
    TrackFeature b =
        createFeature(
            new Cartesian3DTrack(
                CARTESIAN_PARAMETERS,
                new Position(0.0, 6000006.0, 0.0, false),
                new Position(6.0, 6000006.0, 0.0, false),
                new Position(6.0, 6000006.0, 6.0, false)));
    Track ab =
        new Cartesian3DTrack(
            CARTESIAN_PARAMETERS,
            new Position(0.0, 6000003.0, 0.0, false),
            new Position(6.0, 6000003.0, 0.0, false),
            new Position(6.0, 6000003.0, 6.0, false));
    TrackFeature c =
        createFeature(
            new Cartesian3DTrack(
                CARTESIAN_PARAMETERS,
                new Position(-6.0, 6000003.0, -6.0, false),
                new Position(0.0, 6000003.0, -6.0, false),
                new Position(0.0, 6000003.0, 0.0, false)));
    Track abc =
        new Cartesian3DTrack(
            CARTESIAN_PARAMETERS,
            new Position(-2.0, 6000003.0, -2.0, false),
            new Position(4.0, 6000003.0, -2.0, false),
            new Position(4.0, 6000003.0, 4.0, false));

    TrackCentroid centroid = new TrackCentroid();
    centroid.add(a);
    Assert.assertEquals(a.getValue(), getCentroidValue(centroid));
    centroid.add(b);
    Assert.assertEquals(ab, getCentroidValue(centroid));
    centroid.add(c);
    Assert.assertEquals(abc, getCentroidValue(centroid));
    centroid.remove(c);
    Assert.assertEquals(ab, getCentroidValue(centroid));
    centroid.remove(b);
    Assert.assertEquals(a.getValue(), getCentroidValue(centroid));
    centroid.remove(a);
    Assert.assertNull(getCentroidValue(centroid));
  }

  private Track getCentroidValue(TrackCentroid centroid) {
    TrackFeature feature = centroid.getCentroid();
    return feature != null ? feature.getValue() : null;
  }

  private TrackFeature createFeature(Track track) {
    TrackFeature feature = new TrackFeature();
    feature.setValue(track);
    return feature;
  }
}
