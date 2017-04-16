package common.settings

import com.google.inject.{AbstractModule, Provides}
import com.mohiva.play.silhouette.api.crypto.{CookieSigner, Crypter, CrypterAuthenticatorEncoder}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.{Environment, EventBus, Silhouette, SilhouetteProvider}
import com.mohiva.play.silhouette.api.services.{AuthenticatorService, IdentityService}
import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.crypto.{JcaCookieSigner, JcaCookieSignerSettings, JcaCrypter, JcaCrypterSettings}
import com.mohiva.play.silhouette.impl.authenticators.{CookieAuthenticator, CookieAuthenticatorService, CookieAuthenticatorSettings, JWTAuthenticator}
import com.mohiva.play.silhouette.impl.providers.oauth1.TwitterProvider
import com.mohiva.play.silhouette.impl.providers.oauth1.secrets.{CookieSecretProvider, CookieSecretSettings}
import com.mohiva.play.silhouette.impl.providers.oauth1.services.PlayOAuth1Service
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import com.mohiva.play.silhouette.impl.providers._
import com.mohiva.play.silhouette.impl.util.{DefaultFingerprintGenerator, SecureRandomIDGenerator}
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.persistence.repositories.DelegableAuthInfoRepository
import common.auth.logic.UserLogic
import common.auth.models.User
import common.auth.persistence.managers.{OAuth1Manager, PasswordManager, TokenManager, UserCredentialManager}
import common.auth.persistence.managers.traits.TokenManagerTrait
import common.auth.persistence.traits.UserCredentialManagerTrait
import common.settings.auth.MyEnv
import net.codingwell.scalaguice.ScalaModule
import play.api.Configuration

import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by sfrsebastian on 4/15/17.
  */
class CommonModule extends AbstractModule with ScalaModule {

  def configure() {
    bind[Silhouette[MyEnv]].to[SilhouetteProvider[MyEnv]]
    bind[IdentityService[User]].to[UserLogic]
    bind[UserCredentialManagerTrait].to[UserCredentialManager]
    bind[TokenManagerTrait].to[TokenManager]
    bind[DelegableAuthInfoDAO[PasswordInfo]].to[PasswordManager]
    bind[DelegableAuthInfoDAO[OAuth1Info]].to[OAuth1Manager]
    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
    bind[PasswordHasher].toInstance(new BCryptPasswordHasher)
    bind[FingerprintGenerator].toInstance(new DefaultFingerprintGenerator(false))
    bind[EventBus].toInstance(EventBus())
    bind[Clock].toInstance(Clock())
  }

  @Provides
  def provideEnvironment(
                          identityService: UserLogic,
                          authenticatorService: AuthenticatorService[CookieAuthenticator],
                          eventBus: EventBus): Environment[MyEnv] = {
    Environment[MyEnv](
      identityService,
      authenticatorService,
      Seq(),
      eventBus
    )
  }

  @Provides
  def provideAuthenticatorService(
                                   cookieSigner: CookieSigner,
                                   crypter: Crypter,
                                   fingerprintGenerator: FingerprintGenerator,
                                   idGenerator: IDGenerator,
                                   configuration: Configuration,
                                   clock: Clock
                                 ): AuthenticatorService[CookieAuthenticator] = {
    val config = configuration.underlying.as[CookieAuthenticatorSettings]("silhouette.authenticator")
    val encoder = new CrypterAuthenticatorEncoder(crypter)
    new CookieAuthenticatorService(config, None, cookieSigner, encoder, fingerprintGenerator, idGenerator, clock)
  }

  @Provides
  def provideCredentialsProvider(
                                  authInfoRepository: AuthInfoRepository,
                                  passwordHasherRegistry: PasswordHasherRegistry
                                ): CredentialsProvider = {
    new CredentialsProvider(authInfoRepository, passwordHasherRegistry)
  }

  @Provides def provideAuthInfoRepository(
                                           passwordInfoDAO: DelegableAuthInfoDAO[PasswordInfo],
                                           oauth1InfoDAO: DelegableAuthInfoDAO[OAuth1Info]): AuthInfoRepository = {
    new DelegableAuthInfoRepository(passwordInfoDAO, oauth1InfoDAO)
  }

  @Provides
  def providePasswordHasherRegistry(passwordHasher: PasswordHasher): PasswordHasherRegistry = {
    new PasswordHasherRegistry(passwordHasher)
  }

  @Provides
  def provideAuthenticatorCrypter(configuration: Configuration): Crypter = {
    val config = configuration.underlying.as[JcaCrypterSettings]("silhouette.authenticator.crypter")
    new JcaCrypter(config)
  }

  @Provides
  def provideAuthenticatorCookieSigner(configuration: Configuration): CookieSigner = {
    val config = configuration.underlying.as[JcaCookieSignerSettings]("silhouette.authenticator.cookie.signer")
    new JcaCookieSigner(config)
  }

  /*@Provides def provideTwitterProvider(
                                        httpLayer: HTTPLayer,
                                        tokenSecretProvider: OAuth1TokenSecretProvider,
                                        configuration: Configuration): TwitterProvider = {
    val settings = configuration.underlying.as[OAuth1Settings]("silhouette.twitter")
    new TwitterProvider(httpLayer, new PlayOAuth1Service(settings), tokenSecretProvider, settings)
  }

  @Provides
  def provideOAuth1TokenSecretProvider(configuration: Configuration, clock: Clock): OAuth1TokenSecretProvider = {
    val settings = configuration.underlying.as[CookieSecretSettings]("silhouette.oauth1TokenSecretProvider")
    new CookieSecretProvider(settings, clock)
  }

  @Provides def provideSocialProviderRegistry(
                                               twitterProvider: TwitterProvider): SocialProviderRegistry = {
    SocialProviderRegistry(Seq(twitterProvider))
  }*/
}
