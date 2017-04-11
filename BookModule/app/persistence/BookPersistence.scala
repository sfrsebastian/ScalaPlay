package persistence.bookModule

import traits.profiles.{DevProfile, TestProfile}

class BookPersistenceDev extends BookPersistenceTrait with DevProfile
class BookPersistenceTest extends BookPersistenceTrait with TestProfile