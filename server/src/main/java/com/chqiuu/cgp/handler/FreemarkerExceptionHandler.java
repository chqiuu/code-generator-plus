package com.chqiuu.cgp.handler;

import freemarker.core.Environment;
import freemarker.core.StopException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.Writer;

/**
 * Freemarker 统一异常处理
 *
 * @author chqiu
 */
@Slf4j
public class FreemarkerExceptionHandler implements TemplateExceptionHandler {
    /**
     * Method called after a {@link TemplateException} was raised inside a template. The exception should be re-thrown
     * unless you want to suppress the exception.
     *
     * <p>Note that you can check with {@link Environment#isInAttemptBlock()} if you are inside a {@code #attempt}
     * block, which then will handle this exception and roll back the output generated inside it.
     *
     * <p>Note that {@link StopException}-s (raised by {@code #stop}) won't be captured here.
     *
     * <p>Note that you shouldn't log the exception in this method unless you suppress it. If there's a concern that the
     * exception might won't be logged after it bubbles up from {@link Template#process(Object, Writer)}, simply
     * ensure that {@link Configuration#getLogTemplateExceptions()} is {@code true}.
     *
     * @param te  The exception that occurred; don't forget to re-throw it unless you want to suppress it
     * @param env The runtime environment of the template
     * @param out This is where the output of the template is written
     */
    @Override
    public void handleTemplateException(TemplateException te, Environment env, Writer out) throws TemplateException {
        // log.error("[Freemarker出错了，请联系网站管理员]:{} ", te.getMessage());
        /*
        IGNORE_HANDLER 这只是跳过失败的指令，让模板继续执行
        RETHROW_HANDLER 这只是重新抛出异常;这应该在大多数生产系统中使用。
        DEBUG_HANDLER 在开发非html模板时非常有用。此处理程序将堆栈跟踪信息输出到客户端，然后重新抛出异常。
        HTML_DEBUG_HANDLER 在开发HTML模板时非常有用。此处理程序将堆栈跟踪信息输出到客户机，对其进行格式化，使其在浏览器中具有良好的可读性，然后重新抛出异常。
         */
        TemplateExceptionHandler.HTML_DEBUG_HANDLER.handleTemplateException(te, env, out);
    }
}