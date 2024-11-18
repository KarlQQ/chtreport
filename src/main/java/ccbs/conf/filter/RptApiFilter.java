package ccbs.conf.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Filter implementation class RptApiFilter
 */
@WebFilter(filterName = "RptApiFilter", urlPatterns = { "/*" })
@Slf4j
public class RptApiFilter implements Filter {
	// private FilterConfig filterConfig = null;
	/**
	 * Default constructor.
	 */
	public RptApiFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// this.filterConfig = null;
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		// pass the request along the filter chain
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		try {
			//setSessionAttr(request);
		} catch (Exception e) {
			log.error("", e);
		}
		chain.doFilter(request, response);
	}

	protected void setSessionAttr(HttpServletRequest request) {

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
