package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UpdateUserController {
    private static final Logger log = LoggerFactory.getLogger(UpdateUserController.class);

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(String userId, String password, String name, String email, HttpServletRequest request) {
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(userId, password, name, email);
        log.debug("Update User : {}", updateUser);
        user.update(updateUser);
        return ModelAndView.fromJspView("redirect:/");
    }
}
