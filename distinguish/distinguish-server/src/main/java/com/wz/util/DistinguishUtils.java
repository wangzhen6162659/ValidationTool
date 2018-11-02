package com.wz.util;

import com.wz.entity.DistinguishObject;
import com.wz.entity.ImgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Slf4j
//@Component
public class DistinguishUtils {
    private Random generator = new Random();
    private final int TITLE_WIDTH = 60;
    private final String PATH_DEFULT_IMG = "img_distinguish";
    private List<ImgDTO> dtos = new ArrayList<>();

    /**
     * 使用默认图集
     *
     * @param widthNum  列数
     * @param heightNum 行数
     * @return
     */
    public DistinguishObject getDistinguish(int widthNum, int heightNum) {
        return getDistinguishAndPath(widthNum, heightNum);
    }

    public void initImgObejct() {
        loadResource(PATH_DEFULT_IMG);
    }

    public void initImgObejct(String path) {
        loadResource(PATH_DEFULT_IMG);
        if (!StringUtils.isEmpty(path)) {
            loadResource(path);
        }
    }

    public void initImgObejct(String path, boolean useDefult) {
        if (useDefult) {
            loadResource(PATH_DEFULT_IMG);
        }
        if (!StringUtils.isEmpty(path)) {
            loadResource(path);
        }
    }

    private void loadResource(String path) {
        List<ImgDTO> files;
        files = getFilesByJar(path);
        dtos.addAll(files != null ? files : getFileByDisk(path));
        log.info("加载完毕,共" + dtos.size() + "标签");
    }


    /**
     * 执行总逻辑
     *
     * @param widthNum
     * @param heightNum
     * @return
     */
    private DistinguishObject getDistinguishAndPath(int widthNum, int heightNum) {
        //获取默认路径下图形集合
        if (dtos.size() == 0) {
            return new DistinguishObject();
        }
        List<Object> object = getEightImages(dtos, widthNum * heightNum);

        //获取随机图形集合
        InputStream[] finalImages = (InputStream[]) object.get(0);

        //获得随机图形集合的分类名
        String[] tips = (String[]) object.get(1);
        //所有key的图片位置,即用户必须要选的图片
        List<Object> locations = getLocation(Arrays.asList(tips));

        //获取随机分类的下标
        String[] tip = (String[]) locations.get(0);
        locations.remove(0);
        List<Integer> locationsIndex = new ArrayList<>();

        locations.forEach(obj -> {
            locationsIndex.add((Integer) obj);
        });

        DistinguishObject distinguishObject = new DistinguishObject();
        distinguishObject.setClickNum(locations.size());
        distinguishObject.setFinalImages(finalImages);
        distinguishObject.setLocations(locationsIndex);
        distinguishObject.setImg(mergeImage(finalImages, widthNum, heightNum, tip));

        return distinguishObject;
    }

    /**
     * 读取某个文件夹下的所有文件
     */
    public List<ImgDTO> getFileByDisk(String filepath) {
        List<ImgDTO> list = new ArrayList<>();
        Resource fileRource = new ClassPathResource(filepath);

        try {
            if (fileRource.getFile() != null) {
                log.info("当前是寻找磁盘路径文件");
            }
            if (fileRource.getFile().isDirectory()) {
                String[] filelist = fileRource.getFile().list();
                for (int i = 0; i < filelist.length; i++) {
                    fileRource = new ClassPathResource(filepath + "\\" + filelist[i]);
                    if (fileRource.getFile().isDirectory()) {
                        ImgDTO dto = new ImgDTO(filelist[i],null, null);
                        getFileChildByDisk(filepath + "\\" + filelist[i], dto.getChilds());
                        list.add(dto);
                    }
                }
            }
        } catch (IOException e) {
            log.info("无法识别的路径");
        }
        return list;
    }

    /**
     * @param filepath 当前递归路径
     * @param list     文件二维集合
     */
    public void getFileChildByDisk(String filepath, List<ImgDTO> list) {
        Resource fileRource = new ClassPathResource(filepath);
        try {
            if (fileRource.getFile().isDirectory()) {
                String[] filelist = fileRource.getFile().list();
                for (int i = 0; i < filelist.length; i++) {
                    fileRource = new ClassPathResource(filepath + "\\" + filelist[i]);
                    if (!fileRource.getFile().isDirectory()) {
                        list.add(new ImgDTO(fileRource.getFile().getName(), input2byte(fileRource.getInputStream()), fileRource.getInputStream()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取无序String集合
     *
     * @param strs
     * @return
     */
    public String getRandom(List<String> strs) {
        return strs.get(generator.nextInt(strs.size()));
    }

    /**
     * 获取一个范围内随机值
     *
     * @param size
     * @return
     */
    public Integer getRandom(int size) {
        int result = generator.nextInt(size);
        return result;
    }

    /**
     * 获取随机分类标识
     *
     * @param strs
     * @return
     */
    public String[] getTip(List<String> strs) {
        int num = 2;
        String[] tips = new String[num];
        for (int i = 0; i < num; i++) {

            tips[i] = strs.get(generator.nextInt(strs.size()));
        }
        return tips;
    }

    /**
     * 遍历获取文件
     *
     * @param dtos
     * @param name
     * @return
     */
    public ImgDTO getFiles(List<ImgDTO> dtos, String name) {
        return (ImgDTO) dtos.stream().filter(dto -> name.equals(dto.getName())).toArray()[0];
    }

    /**
     * 随机选取N个图片
     *
     * @param dtoList ImgDTO 二维集合,[分类][图片]
     * @param imgNum  N
     * @return
     */
    private List<Object> getEightImages(List<ImgDTO> dtoList, Integer imgNum) {
        InputStream[] finalImages = new InputStream[imgNum];
        List<Object> object = new ArrayList<Object>();
        List<String> isEx = new ArrayList<>();

        //保存tips
        String[] tips = new String[imgNum];
        int i = 0;
        List<String> imgNames = dtoList.stream().map(ImgDTO::getName).collect(Collectors.toList());
        while (i < imgNum) {
            //获取随机的二级目录
            String dirIndexName = getRandom(imgNames);
            ImgDTO secondaryDir = getFiles(dtoList, dirIndexName);


            //获取二级图片目录下的文件
            List<ImgDTO> dtosChild = secondaryDir.getChilds();
            List<byte[]> imagesBytes = dtosChild.stream().map(ImgDTO::getBytes).collect(Collectors.toList());
            List<InputStream> images = byte2Input(imagesBytes);
            List<String> names = dtosChild.stream().map(ImgDTO::getName).collect(Collectors.toList());
            int imageIndex = getRandom(images.size());

            //去除重复图像
            InputStream image = images.get(imageIndex);
            if (isEx.contains(secondaryDir.getName() + "." + names.get(imageIndex))) {
                continue;
            }

            //随机到的文件夹名称保存到tips中
            tips[i] = secondaryDir.getName();
            finalImages[i] = image;
            isEx.add(secondaryDir.getName() + "." + names.get(imageIndex));
            i++;
        }
        object.add(finalImages);
        object.add(tips);
        return object;
    }

    /**
     * 获取预选标识位置
     *
     * @param tips 标识集合
     * @return
     */
    public List<Object> getLocation(List<String> tips) {
        List<Object> locations = new ArrayList<Object>();

        //获取Key分类
        Set<String> tipSet = new HashSet<String>(Arrays.asList(getTip(tips)));
        String[] tipArr = new String[tipSet.size()];
        new ArrayList<String>(tipSet).toArray(tipArr);
        locations.add(tipArr);

        int length = tips.size();
        for (int i = 0; i < length; i++) {
            if (tipSet.contains(tips.get(i))) {
                locations.add(i + 1);
            }
        }

        return locations;
    }

    /**
     * is流的复制
     *
     * @param input
     * @return
     */
    private static InputStream[] cloneInputStream(InputStream input) {
        InputStream[] iso = new InputStream[2];
        InputStream is;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            iso[0] = new ByteArrayInputStream(baos.toByteArray());
            iso[1] = new ByteArrayInputStream(baos.toByteArray());
            return iso;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 合成验证图片
     *
     * @param finalImages 所需图片集合
     * @param widthNum    横坐标量
     * @param heightNum   纵坐标量
     * @throws IOException
     */
    public BufferedImage mergeImage(InputStream[] finalImages, int widthNum, int heightNum, String[] tips) {

        //初始化
        BufferedImage mergeImage = new BufferedImage(widthNum * 200, TITLE_WIDTH + heightNum * 200, BufferedImage.TYPE_INT_BGR);

        int index = 0;
        for (int i = 0; i < heightNum; i++) {
            for (int j = 0; j < widthNum; j++) {
                index = i * widthNum + j;
                InputStream images = finalImages[index];
                BufferedImage bufferedImage = null;
                try {
                    bufferedImage = ImageIO.read(images);
                    if (bufferedImage == null) {
                        continue;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                //从图片中读取RGB
                int[] imageBytes = new int[width * height];
                imageBytes = bufferedImage.getRGB(0, 0, width, height, imageBytes, 0, width);
                mergeImage.setRGB(j * 200, TITLE_WIDTH + i * 200, width, height, imageBytes, 0, width);
            }
        }

        //读取头文字信息
        mergeImage = imageCode(mergeImage, tips, widthNum * 200, (heightNum * 200) / 4);


        return mergeImage;
    }

    /**
     * 创建横幅
     *
     * @param image  图片buffer
     * @param str    关键字
     * @param width  宽
     * @param height 高
     * @return
     */
    public BufferedImage imageCode(BufferedImage image, String str[], int width, int height) {
        // 在内存中创建图象
//        BufferedImage image = new BufferedImage(width, height,
//                BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        // 设定背景色
        g.setColor(new Color(250, 250, 250));
        g.fillRect(0, 0, width, TITLE_WIDTH);
        // 设定字体
        int fontSize = 30;
        g.setFont(new Font("宋体", Font.ITALIC, fontSize));

        //预设标题
        String title = "请识别出下列所有的:";
        g.setColor(new Color(0, 0, 0));
        g.drawString(title, 20, (TITLE_WIDTH + fontSize) / 2);
        int spacing = 0;
        int titleSize = title.length() * fontSize + 10;
        for (int i = 0; i < str.length; i++) {
            // 将tip添加在标题后面
            int lastTipLocal = spacing + titleSize;
            spacing += str[i].length() * fontSize * 1.5;
            g.setColor(getRandColor(0, 150));
            g.drawString(String.valueOf(str[i]), titleSize + spacing, (TITLE_WIDTH + fontSize) / 2);
            shear(g, lastTipLocal, titleSize + spacing, TITLE_WIDTH);
        }
        for (int i = 0; i < (generator.nextInt(5) + 10); i++) {
            g.setColor(new Color(generator.nextInt(255) + 1, generator.nextInt(255) + 1, generator.nextInt(255) + 1));
            g.drawLine(generator.nextInt(width - titleSize) + titleSize, generator.nextInt(TITLE_WIDTH),
                    generator.nextInt(width - titleSize) + titleSize, generator.nextInt(TITLE_WIDTH));
        }
        // 图象生效
        g.dispose();
        return image;
    }

    /**
     * 获得随机颜色
     *
     * @param begin 范围起点
     * @param end   范围末
     * @return
     */
    private Color getRandColor(int begin, int end) {
        if (begin > 255)
            begin = 255;
        if (end > 255)
            end = 255;
        int r = begin + generator.nextInt(end - begin);
        int g = begin + generator.nextInt(end - begin);
        int b = begin + generator.nextInt(end - begin);
        return new Color(r, g, b);
    }

    /**
     * 图像偏移
     *
     * @param g
     * @param beginX
     * @param w1
     * @param h1
     */
    private void shear(Graphics g, int beginX, int w1, int h1) {

        shearX(g, beginX, w1, h1);
        shearY(g, beginX, w1, h1);
    }

    /**
     * byte转is
     * @param buf
     * @return
     */
    public static final InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    /**
     * byte转is
     * @param buf
     * @return
     */
    public static final List<InputStream> byte2Input(List<byte[]> bufs) {
        List<InputStream> inputStreams = new ArrayList<>();
        for (int i=0; i<bufs.size(); i++){
            inputStreams.add(byte2Input(bufs.get(i)));
        }
        return inputStreams;
    }

    /**
     * is转byte[]
     * @param inStream
     * @return
     * @throws IOException
     */
    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    /**
     * 图像偏移X
     *
     * @param g
     * @param beginX
     * @param w1
     * @param h1
     */
    private void shearX(Graphics g, int beginX, int w1, int h1) {
        float tag = generator.nextFloat();
        int negativeTag = tag > 0.5 ? 1 : -1;

        int period = (int) (generator.nextFloat() * 4 * (-negativeTag));

        int frames = 1;
        int phase = 1;

        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / frames);
            g.copyArea(beginX, i, w1, 1, (int) d * negativeTag, 0);
        }
    }

    /**
     * 图像偏移Y
     *
     * @param g
     * @param beginX
     * @param w1
     * @param h1
     */
    private void shearY(Graphics g, int beginX, int w1, int h1) {
        float tag = generator.nextFloat();
        int negativeTag = tag > 0.5 ? 1 : -1;

        int period = generator.nextInt(3) + 10;

        int frames = 1;
        int phase = 1;
        for (int i = 0; i < w1 + 100 - beginX; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(beginX + i, 0, 1, h1 - 5, 0, (int) d * negativeTag);
        }
    }

    private List<ImgDTO> getFilesByJar(String imgPath) {
        Enumeration<JarEntry> jarEntrys = getJarEntrys(imgPath);
        if (jarEntrys == null) {
            return null;
        }
        log.info("当前是寻找jar路径文件");
        List<ImgDTO> dtos = new ArrayList<>();
        while (jarEntrys.hasMoreElements()) {
            JarEntry entry = jarEntrys.nextElement();
            String name = entry.getName();
            log.info("父path下的name" + name);
            if (entry.isDirectory() && name.startsWith(imgPath, name.indexOf(imgPath)) && !name.endsWith(imgPath + "/")) {
                ImgDTO imgDTO = new ImgDTO();
                String tipName = name.substring(name.indexOf(imgPath) + imgPath.length() + 1, name.length() - 1);
                imgDTO.setName(tipName);
                getChildFilesByJar(imgDTO.getChilds(), name.substring(0, name.length() - 1));
                dtos.add(imgDTO);
            }
        }
        return dtos;
    }

    /**
     * @param imgPath
     */
    private void getChildFilesByJar(List<ImgDTO> dtos, String imgPath) {
        Enumeration<JarEntry> jarEntrys = getJarEntrys(imgPath);
        log.info(imgPath);
        while (jarEntrys.hasMoreElements()) {
            JarEntry entry = jarEntrys.nextElement();
            String name = entry.getName();
            if (!entry.isDirectory() && name.startsWith(imgPath, name.indexOf(imgPath))) {
                ImgDTO imgDTO = new ImgDTO();
                String tipName = name.substring(name.indexOf(imgPath) + imgPath.length() + 1, name.lastIndexOf("."));
                imgDTO.setName(tipName);
                imgDTO.setFile(getFile(imgPath + "/" + name.substring(name.indexOf(imgPath) + imgPath.length() + 1, name.length())));
                try {
                    imgDTO.setBytes(input2byte(imgDTO.getFile()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dtos.add(imgDTO);
            }
        }
    }

    /**
     * 获得jar中映射元素
     *
     * @param imgPath
     * @return
     */
    private Enumeration<JarEntry> getJarEntrys(String imgPath) {
        URL url = DistinguishUtils.class.getClassLoader().getResource(imgPath + "/");
        String jarPath = url.toString();
        if (jarPath.indexOf("!/") != -1) {
            jarPath = jarPath.substring(0, jarPath.indexOf("!/") + 2);
        }
        JarURLConnection jarCon = null;
        Enumeration<JarEntry> jarEntrys = null;

        try {
            URL urlObj = new URL(jarPath);
            URLConnection urlConnection = urlObj.openConnection();
            if (urlConnection instanceof JarURLConnection) {
                jarCon = (JarURLConnection) urlConnection;
                JarFile jarFile = jarCon.getJarFile();
                jarEntrys = jarFile.entries();
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jarEntrys;
    }

    /**
     * 获取目标流
     *
     * @param imgPath
     * @return
     */
    private InputStream getFile(String imgPath) {
        log.info("准备：" + imgPath);
        InputStream inputStream = DistinguishUtils.class.getClassLoader().getResourceAsStream(imgPath);
        if (inputStream == null) {
            log.info("什么情况：" + imgPath);
        }
        return inputStream;
    }

}
