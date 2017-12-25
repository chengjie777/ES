package cn.elasticsearch.web;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration({"classpath:applicationContext.xml"})
	public abstract class SearchBaseTest extends AbstractJUnit4SpringContextTests{
	}
