/**
 * Copyright (c) 2017-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.litho;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.StyleRes;

import com.facebook.yoga.YogaMeasureMode;
import com.facebook.yoga.YogaMeasureFunction;
import com.facebook.yoga.YogaNode;
import com.facebook.yoga.YogaMeasureOutput;

/**
 * {@link ComponentContext} for use within a test environment that is compatible with mock
 * ComponentSpecs in addition to real implementation.
 */
class TestComponentContext extends ComponentContext {

  private static final YogaMeasureFunction FAKE_MEASURE_FUNCTION = new YogaMeasureFunction() {
    @Override
    public long measure(
        YogaNode cssNode,
        float width,
        YogaMeasureMode widthMode,
        float height,
        YogaMeasureMode heightMode) {
      return YogaMeasureOutput.make(1, 1);
    }
  };

  TestComponentContext(Context c) {
    super(c);
  }

  TestComponentContext(Context c, StateHandler stateHandler) {
    super(c, stateHandler);
  }

  /**
   * Uses the provided component to create a layout. Only the root node is created using
   * {@link ComponentLifecycle#onCreateLayout(ComponentContext, Component)} - below that, we just
   * create a dummy internal node and set the lifecycle to it.
   */
  @Override
  public ComponentLayout.Builder newLayoutBuilder(Component<?> component) {
    final InternalNode node = ComponentsPools.acquireInternalNode(this, getResources());
    component.applyStateUpdates(this);

    node.appendComponent(new TestComponent(component));

    return node;
  }

  @Override
  public ComponentLayout.Builder newLayoutBuilder(
      Component<?> component,
      @AttrRes int defStyleAttr,
      @StyleRes int defStyleRes) {
    return newLayoutBuilder(component);
  }

  @Override
  TestComponentContext makeNewCopy() {
    return new TestComponentContext(this);
  }
}
