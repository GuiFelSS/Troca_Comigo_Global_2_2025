package br.com.fiap.globalSolution.Security;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Configuration
public class AppConfig {

    /**
     * Bean 1: MessageSource
     * Diz ao Spring ONDE encontrar os arquivos de tradução.
     * Ele vai procurar por "messages.properties", "messages_pt_BR.properties", etc.
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // Define o caminho base para os arquivos de properties
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Bean 2: LocaleResolver
     * Define COMO o Spring vai descobrir qual idioma o usuário quer.
     * "AcceptHeaderLocaleResolver" é o padrão para APIs: ele olha o header "Accept-Language"
     * que o navegador ou app mobile envia automaticamente.
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        // Define o idioma padrão caso o usuário não envie o header
        localeResolver.setDefaultLocale(new Locale("pt", "BR"));
        return localeResolver;
    }
}