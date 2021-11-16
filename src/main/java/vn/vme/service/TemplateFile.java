package vn.vme.service;

import java.util.Map;

import vn.vme.common.Utils;

public class TemplateFile {
	
	private String templateFile;
	private Map<String, String> values;

	public TemplateFile(String templateFile, Map<String, String> values) {
		this.templateFile = templateFile;
		this.values = values;
	}

	public String render() {
		String template = Utils.readAsText(String.format("template/%s", templateFile));

		for (Map.Entry<String, String> entry : values.entrySet()) {
			template = template.replace(String.format("${%s}", entry.getKey()), entry.getValue());
		}
		return template;
	}

	public static String render(String templateFile, Map<String, String> values) {
		TemplateFile engine = new TemplateFile(templateFile, values);
		return engine.render();
	}
}
