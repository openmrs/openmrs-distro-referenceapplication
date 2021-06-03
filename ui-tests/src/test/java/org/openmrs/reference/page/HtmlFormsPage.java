package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class HtmlFormsPage extends Page {
    private static final By NEW_HTML_FORM = By.cssSelector("a[href='htmlForm.form']");
    private static final By NAME = By.name("form.name");
    private static By DESCRIPTION = By.name("form.description");
    private static By SAVE_FORM = By.cssSelector("input[type='submit']");
    private static By VERSION = By.name("form.version");

    public HtmlFormsPage(Page page) {
        super(page);
    }

    public void createNewFormTest(String name, String description, String version) throws InterruptedException {
        setTextToFieldNoEnter(NAME, name);
        setTextToFieldNoEnter(DESCRIPTION, description);
        setTextToFieldNoEnter(VERSION, version);
        clickOn(SAVE_FORM);
    }

    public String getPageUrl() {
        return "/module/htmlformentry/htmlForm.form";
    }
}
