package persistence.bookModule

import common.traits.persistenceProfiles.{DevProfile, TestProfile}

class BookPersistenceDev extends BookPersistenceTrait with DevProfile
class BookPersistenceTesting extends BookPersistenceTrait with TestProfile