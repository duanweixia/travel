package cn.itcast.travel.web.servlet;

import cn.itcast.travel.daomain.PageBean;
import cn.itcast.travel.daomain.Route;
import cn.itcast.travel.daomain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @auther lizongxiao
 * @date 2019/9/12 - 12:03
 */
@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    //分页查询
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        //接受rname 线路名称
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"), "utf-8");

        int cid = 0;//类别id
        //2.处理参数
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }

        int currentPage = 0;//当前页码，如果不传递，则默认为第一页
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }

        int pageSize = 0;//每页显示条数，如果不传递，则默认显示5条记录
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }

        //3.调用service查询PageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize, rname);

        //4.将PageBean对象序列化为json，返回
        writeValue(pb, response);
    }

    //根据id查询一个旅游线路的详情信息
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收id
        String rid = request.getParameter("rid");
        //2.调用service查询route对象
        Route route = routeService.findOne(rid);
        //3.转为json写回客户端
        writeValue(route,response);
    }

    //根据cid查询
    public void findByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收cid
        String cid = request.getParameter("cid");
        //2.调用service查询route对象
        List<Route> routes = routeService.findByCid(Integer.parseInt(cid));
        //3.转为json写回客户端
        writeValue(routes,response);
    }


    //根据id查询一个旅游线路的详情信息
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取线路id
        String rid = request.getParameter("rid");
        //2.获取当前登录的用户 user
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户
        if (user == null){
            //用户尚未登录
            uid = 0;
        }else{
            //用户已经登陆
            uid = user.getUid();
        }
        //3.调用FavriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);
        //4.写回客户端
        writeValue(flag,response);
    }

    //添加客户端
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取线路id
        String rid = request.getParameter("rid");
        //2.获取当前登录的用户 user
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if (user == null){
            //用户尚未登录
            uid = 0;
        }else{
            //用户已经登陆
            uid = user.getUid();
        }
        //3.调用service添加
        favoriteService.add(rid,uid);

    }

}