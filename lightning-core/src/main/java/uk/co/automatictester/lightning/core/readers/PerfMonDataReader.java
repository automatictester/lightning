package uk.co.automatictester.lightning.core.readers;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.csv.CsvParserSettings;

public class PerfMonDataReader implements CsvDataReader {

    private BeanListProcessor<PerfMonBean> rowProcessor = new BeanListProcessor<>(PerfMonBean.class);

    @Override
    public BeanListProcessor<PerfMonBean> beanListProcessor() {
        return rowProcessor;
    }

    @Override
    public CsvParserSettings csvParserSettings() {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setLineSeparatorDetectionEnabled(true);
        parserSettings.setHeaderExtractionEnabled(false);
        ConcurrentRowProcessor concurrentRowProcessor = new ConcurrentRowProcessor(beanListProcessor());
        parserSettings.setProcessor(concurrentRowProcessor);
        return parserSettings;
    }
}
