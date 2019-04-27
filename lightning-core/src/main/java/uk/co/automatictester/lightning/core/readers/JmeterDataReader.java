package uk.co.automatictester.lightning.core.readers;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.csv.CsvParserSettings;

public class JmeterDataReader implements CsvDataReader {

    private BeanListProcessor<JmeterBean> rowProcessor = new BeanListProcessor<>(JmeterBean.class);

    @Override
    public BeanListProcessor<JmeterBean> beanListProcessor() {
        return rowProcessor;
    }

    @Override
    public CsvParserSettings csvParserSettings() {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setLineSeparatorDetectionEnabled(true);
        parserSettings.setHeaderExtractionEnabled(true);
        ConcurrentRowProcessor concurrentRowProcessor = new ConcurrentRowProcessor(beanListProcessor());
        parserSettings.setProcessor(concurrentRowProcessor);
        return parserSettings;
    }
}
