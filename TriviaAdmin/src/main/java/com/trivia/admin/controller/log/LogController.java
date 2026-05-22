package com.trivia.admin.controller.log;

import com.trivia.core.utility.LogType;
import com.trivia.core.utility.LogUtil;
import com.trivia.persistence.dto.server.Log;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class LogController implements Serializable {
    private LazyDataModel<Log> lazyLogs;

    @PostConstruct
    public void init() {
        lazyLogs = new LazyDataModel<Log>() {
            @Override
            public List<Log> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Log> logs = new ArrayList<>();

                try {
                    logs = LogUtil.readFromTo(first, pageSize, LogType.ADMIN);
                    lazyLogs.setRowCount(Math.toIntExact(LogUtil.getLineCount(LogType.ADMIN)));
                }
                catch (IOException e) {

                }
                catch (ArithmeticException e) { // Meaning the count of logs is larger than int max value. Won't really be needed.
                    lazyLogs.setRowCount(Integer.MAX_VALUE);
                }
                return logs;
            }
        };
    }

    public LazyDataModel<Log> getLazyLogs() {
        return lazyLogs;
    }

    public void setLazyLogs(LazyDataModel<Log> lazyLogs) {
        this.lazyLogs = lazyLogs;
    }
}