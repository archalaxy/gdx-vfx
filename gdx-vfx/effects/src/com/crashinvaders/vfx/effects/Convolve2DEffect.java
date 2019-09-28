/*******************************************************************************
 * Copyright 2019 metaphore
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.crashinvaders.vfx.effects;

import com.crashinvaders.vfx.VfxRenderContext;
import com.crashinvaders.vfx.framebuffer.PingPongBuffer;

/**
 * Encapsulates a separable 2D convolution kernel filter
 *
 * @author bmanuel
 * @author metaphore
 */
public final class Convolve2DEffect extends CompositeVfxEffect {

    private final int radius;
    private final int length; // NxN taps filter, w/ N=length
    private final float[] weights, offsetsHor, offsetsVert;

    private Convolve1DEffect hor, vert;

    public Convolve2DEffect(int radius) {
        this.radius = radius;
        length = (radius * 2) + 1;

        hor = register(new Convolve1DEffect(length));
        vert = register(new Convolve1DEffect(length, hor.weights));

        weights = hor.weights;
        offsetsHor = hor.offsets;
        offsetsVert = vert.offsets;
    }

    @Override
    public void render(VfxRenderContext context, PingPongBuffer pingPongBuffer) {
        hor.render(context, pingPongBuffer);
        pingPongBuffer.swap();
        vert.render(context, pingPongBuffer);
    }

    public int getRadius() {
        return radius;
    }

    public int getLength() {
        return length;
    }

    public float[] getWeights() {
        return weights;
    }

    public float[] getOffsetsHor() {
        return offsetsHor;
    }

    public float[] getOffsetsVert() {
        return offsetsVert;
    }
}
