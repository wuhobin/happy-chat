package com.wuhobin.entity.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:用户上下线变动的推送类
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-03-19
 * @author admin
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WsOnlineOfflineNotify<T> {

    private List<T> changeList = new ArrayList<>();

    private Long onlineNum;


}
