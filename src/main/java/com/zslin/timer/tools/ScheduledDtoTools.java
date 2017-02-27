package com.zslin.timer.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/22 10:43.
 */
public class ScheduledDtoTools {

    private static List<ScheduledDto> dtoList;
    private static ScheduledDtoTools instance;
    public static ScheduledDtoTools getInstance() {
        if(instance==null) {
            instance = new ScheduledDtoTools();
        }
        return instance;
    }
    private ScheduledDtoTools() {
        dtoList = new ArrayList<>();
    }

    public void add(ScheduledDto dto) {
        dtoList.add(dto);
    }

    /**
     * 添加新任务
     * @param dto 任务dto对象
     * @param remove 为true时删除id对应的任务后重新添加
     */
    public void add(ScheduledDto dto, boolean remove) {
        if(remove) {
            remove(dto.getId());
        }
        add(dto);
    }

    public List<ScheduledDto> getDtoList() {
        return dtoList;
    }

    public void remove(String id) {
        for(ScheduledDto dto : dtoList) {
            if(dto.getId().equals(id)) {
                dto.getFuture().cancel(false);
//                dtoList.remove(dto); //移出会抛异常
            }
        }
    }
}
