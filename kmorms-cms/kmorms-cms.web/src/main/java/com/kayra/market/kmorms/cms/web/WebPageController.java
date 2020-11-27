package com.kayra.market.kmorms.cms.web;

import static com.kayra.market.kmorms.cms.integration.security.web.common.ViewConstants.PUB_INPUT;
import static com.kayra.market.kmorms.cms.integration.security.web.common.ViewConstants.PUB_OUTPUT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kayra.market.kmorms.cms.documentmodel.WebPage;
import com.kayra.market.kmorms.cms.integration.service.KmormsAuthenticationService;
import com.kayra.market.kmorms.cms.service.WebPageService;
import com.kayra.market.kmorms.model.admin.AdminLoginStatus;
import com.kayra.market.kmorms.model.validation.ValidationErrorMessages;

import net.rossillo.spring.web.mvc.CacheControl;
import net.rossillo.spring.web.mvc.CachePolicy;

@RequestMapping("/cms/webpage")
@RestController
public class WebPageController {
	
	@Autowired
    private KmormsAuthenticationService kmormsAuthenticationService;
	
	@Autowired
	private WebPageService webPageService;

    @ResponseBody
    @CacheControl(policy = CachePolicy.NO_STORE)
    @RequestMapping(value = "/authenticated", method = RequestMethod.GET, consumes = PUB_INPUT, produces = PUB_OUTPUT)
    public ResponseEntity<Boolean> outletLevelAuthenticated(HttpServletRequest request, HttpServletResponse response) {

        Cookie session = null;
        Cookie[] cookies = request.getCookies();

        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    session = cookie;
                } else if (cookie.getName().equals("XS-SEC-TOKEN")) {
                    session = cookie;
                }
            }
        }

        if (null == session) {
            return new ResponseEntity<>(false, new HttpHeaders(), HttpStatus.OK);
        }

        AdminLoginStatus sellerLoginStatus = kmormsAuthenticationService.authenticate(session.getValue());

        if ((null == sellerLoginStatus)) {
            AdminLoginStatus adminLoginStatus = kmormsAuthenticationService.authenticateAdmin(session.getValue());
            if (null == adminLoginStatus) {
                return new ResponseEntity<>(false, new HttpHeaders(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(true, new HttpHeaders(), HttpStatus.OK);
    }
    
    @ResponseBody
    @RequestMapping(value = "/view", method = RequestMethod.GET, produces = "application/json")
    public List<WebPage> getWebPage() {
        return webPageService.fetchAllWebPages();
    }
    
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = PUB_INPUT, produces = PUB_OUTPUT)
    public ResponseEntity<Object> saveWebPage(@RequestBody WebPage webPage) {
        try {
            return new ResponseEntity<Object>(webPageService.save(webPage), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception nfe) {
            return responseEntityShow(null, "Not available", UNAUTHORIZED);
        }
    }
    
    private ResponseEntity<Object> responseEntityShow(String field, String response, HttpStatus status) {
        ValidationErrorMessages errors = new ValidationErrorMessages();
        errors.addError(field, response, response);
        return new ResponseEntity<Object>(errors, new HttpHeaders(), status);
    }

}
