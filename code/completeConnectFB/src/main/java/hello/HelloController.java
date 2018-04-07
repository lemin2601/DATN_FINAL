package hello;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;

@Controller
@RequestMapping("/")
public class HelloController {

    private Facebook facebook;
    private ConnectionRepository connectionRepository;

    public HelloController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }

    @GetMapping
    public String helloFacebook(Model model) {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return "redirect:/connect/facebook";
        }

        String [] fields = {
                "id",
                "cover",
                "name",
                "first_name",
                "last_name",
                "age_range",
                "link",
                "gender",
                "locale",
                "picture",
                "timezone",
                "updated_time",
                "verified",
                "email"
        };
        User userProfile = facebook.fetchObject("me", User.class, fields);
        System.out.println(userProfile.getId());
        System.out.println(userProfile.getAgeRange().getMin());
        System.out.println(userProfile.getName());
        System.out.println(userProfile.getFirstName());
        System.out.println(userProfile.getLastName());
        System.out.println(userProfile.getGender());
        System.out.println(userProfile.getLink());
        System.out.println(userProfile.getLocale().getLanguage());
        System.out.println(userProfile.getLocale().getCountry());
        System.out.println(userProfile.getEmail());
        LinkedHashMap picture = (LinkedHashMap)userProfile.getExtraData()
                                                          .get("picture");
        LinkedHashMap dataPicture =(LinkedHashMap) picture.get("data");

        System.out.println(dataPicture.get("width") + "|" + dataPicture.get("height") + "|" +dataPicture.get("url") );

        model.addAttribute("facebookProfile",userProfile);// facebook.userOperations().getUserProfile());
        PagedList<Post> feed = facebook.feedOperations().getFeed();
        model.addAttribute("feed", feed);
        return "hello";
    }

}
