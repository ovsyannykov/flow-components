/*
 * Copyright 2000-2025 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component.combobox.test.validation;

import static com.vaadin.flow.component.combobox.test.validation.MultiSelectComboBoxBasicValidationPage.ENABLE_CUSTOM_VALUE_BUTTON;
import static com.vaadin.flow.component.combobox.test.validation.MultiSelectComboBoxBasicValidationPage.REQUIRED_BUTTON;
import static com.vaadin.flow.component.combobox.test.validation.MultiSelectComboBoxBasicValidationPage.REQUIRED_ERROR_MESSAGE;

import org.junit.Test;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.combobox.testbench.MultiSelectComboBoxElement;
import com.vaadin.flow.testutil.TestPath;
import com.vaadin.tests.validation.AbstractValidationIT;

@TestPath("vaadin-multi-select-combo-box/validation/basic")
public class MultiSelectComboBoxBasicValidationIT
        extends AbstractValidationIT<MultiSelectComboBoxElement> {

    @Test
    public void fieldIsInitiallyValid() {
        assertClientValid();
        assertServerValid();
        assertErrorMessage(null);
    }

    @Test
    public void triggerBlur_assertValidity() {
        testField.sendKeys(Keys.TAB);
        assertValidationCount(0);
        assertServerValid();
        assertClientValid();
        assertErrorMessage(null);
    }

    @Test
    public void required_triggerBlur_assertValidity() {
        $("button").id(REQUIRED_BUTTON).click();

        testField.sendKeys(Keys.TAB);
        assertValidationCount(0);
        assertServerValid();
        assertClientValid();
        assertErrorMessage(null);
    }

    @Test
    public void required_changeValue_assertValidity() {
        $("button").id(REQUIRED_BUTTON).click();

        testField.selectByText("foo");
        assertValidationCount(1);
        assertServerValid();
        assertClientValid();
        assertErrorMessage("");

        testField.deselectAll();
        assertValidationCount(1);
        assertServerInvalid();
        assertClientInvalid();
        assertErrorMessage(REQUIRED_ERROR_MESSAGE);
    }

    @Test
    public void required_enterCustomValue_assertValidity() {
        $("button").id(REQUIRED_BUTTON).click();

        testField.sendKeys("custom", Keys.ENTER);
        assertValidationCount(0);
        assertServerValid();
        assertClientValid();
        assertErrorMessage(null);
    }

    @Test
    public void required_customValuesAllowed_enterCustomValue_assertValidity() {
        $("button").id(REQUIRED_BUTTON).click();
        $("button").id(ENABLE_CUSTOM_VALUE_BUTTON).click();

        testField.sendKeys("custom", Keys.ENTER);
        assertValidationCount(1);
        assertServerValid();
        assertClientValid();
        assertErrorMessage("");

        testField.deselectAll();
        assertValidationCount(1);
        assertServerInvalid();
        assertClientInvalid();
        assertErrorMessage(REQUIRED_ERROR_MESSAGE);
    }

    @Test
    public void detach_attach_preservesInvalidState() {
        // Make field invalid
        $("button").id(REQUIRED_BUTTON).click();
        testField.selectByText("foo");
        testField.deselectAll();

        detachAndReattachField();

        assertServerInvalid();
        assertClientInvalid();
    }

    @Test
    public void detach_attachAndInvalidate_preservesInvalidState() {
        detachField();
        attachAndInvalidateField();

        assertServerInvalid();
        assertClientInvalid();
    }

    @Test
    public void detach_hide_attach_showAndInvalidate_preservesInvalidState() {
        detachField();
        hideField();
        attachField();
        showAndInvalidateField();

        assertServerInvalid();
        assertClientInvalid();
    }

    @Test
    public void clientSideInvalidStateIsNotPropagatedToServer() {
        // Make the field invalid
        $("button").id(REQUIRED_BUTTON).click();
        testField.selectByText("foo");
        testField.deselectAll();

        executeScript("arguments[0].invalid = false", testField);

        assertServerInvalid();
    }

    @Override
    protected MultiSelectComboBoxElement getTestField() {
        return $(MultiSelectComboBoxElement.class).first();
    }
}
