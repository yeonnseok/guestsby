package com.brtrip.path.domain

import com.brtrip.path.Path
import org.springframework.data.jpa.repository.JpaRepository

interface PathRepository : JpaRepository<Path, Long> {

}