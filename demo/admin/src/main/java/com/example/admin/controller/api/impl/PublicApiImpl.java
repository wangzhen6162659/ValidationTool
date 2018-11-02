package com.example.admin.controller.api.impl;

import com.wz.entity.DistinguishObject;
import com.wz.util.DistinguishUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("img")
public class PublicApiImpl {
    @Autowired
    protected RedisTemplate redisTemplate;
    @Autowired
    private DistinguishUtils distinguishUtils;

    private static final int HEIGHT_NUM = 3;
    private static final int WIDTH_NUM = 3;

    /**
     * 生成随机图像验证码
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public void getImg(HttpServletResponse response) throws IOException {
        HashOperations hashOperations = redisTemplate.opsForHash();
        response.setContentType("image/jpeg");
        response.setHeader("access-Control-Expose-Headers", "yanzheng_tpi");

        //执行工具类
        DistinguishObject distinguishObject = distinguishUtils.getDistinguish(HEIGHT_NUM,WIDTH_NUM);


        //生成图像
        Map verifyMap = new HashMap();

        response.setHeader("yanzheng_tpi", distinguishObject.getClickNum() + "," + WIDTH_NUM);
        verifyMap.put("locations", distinguishObject.getLocations());
        hashOperations.putAll("verify_1", verifyMap);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try

        {
            ImageIO.write(distinguishObject.getImg(), "jpg", out);
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }
        OutputStream os = response.getOutputStream();
        os.write(out.toByteArray());
        os.flush();
        os.close();
    }

    /**
     * 验证码输入坐标验证 (这里前端默认为,300*150的坐标系(4*75, 2*75),如果前端更改尺寸需要带入baseSize参数,且图像长宽要一致)
     *
     * @param locals
     * @param baseSize
     * @return
     */
    @RequestMapping(value = "yanzheng", method = RequestMethod.GET)
    public boolean yanzheng(@RequestParam(value = "locals[]") Integer[] locals, @RequestParam(value = "baseSize", required = false, defaultValue = "75") Float baseSize) {
//        response.setHeader("access-Control-Allow-Origin","*");
//        response.setHeader("Access-Control-Allow-Credentials","true");

        //获取session保存域
//        HttpSession session = request.getSession();
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (!hashOperations.hasKey("verify_1", "locations")) {
            return false;
        }
        List<Integer> locations = (List<Integer>) hashOperations.get("verify_1", "locations");
        List<Integer> localsList = Arrays.asList(locals);
        //验证
        if (localsList.size() != locations.size() * 2) {
            return false;
        }

        int indexX = 0;
        int indexY = 0;

        for (int i = locations.size() - 1; i >= 0; i--) {
            for (int j = localsList.size() - 1; j >= 0; j--) {
                indexX = (locations.get(i) - 1) % (WIDTH_NUM);
                indexY = (locations.get(i) - 1) / (WIDTH_NUM);
                if (localsList.get(j - 1) >= (indexX) * baseSize && localsList.get(j) >= indexY * baseSize
                        && localsList.get(j - 1) <= (indexX + 1) * baseSize && localsList.get(j) <= (indexY + 1) * baseSize) {
                    locations.remove(locations.get(i));
                    break;
                }
                j--;
            }
        }
        if (locations.size() == 0) {
            return true;
        }
        return false;
    }
}
