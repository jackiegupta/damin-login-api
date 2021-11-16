package vn.vme.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class FeignBeanProcessor implements BeanFactoryPostProcessor {
	/**
	 * Fix close exception JUNIT
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		BeanDefinition bd = beanFactory.getBeanDefinition("feignContext");
		//bd.setDependsOn("eurekaServiceRegistry", "inetUtils");
	}
}