package com.quest.etna.model;


import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Entity
@Data
public class Book {

@Id
@GeneratedValue(strategy=GenerationType.AUTO)
int id;

@Column(length = 50, nullable = false)
String title;

@Column(length = 300, nullable = false)
String description;

@Column(length = 30, nullable = false)
String author;

int pages;

int quantity;

String image;

@Column(name = "date_publication")
Date date_publication;

@Column(name = "creation_date")
@CreationTimestamp
@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
Date creation_date;

@Column(name = "last_modification_date")
@UpdateTimestamp
@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
Date last_modification_date;

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Book other = (Book) obj;
	if (author == null) {
		if (other.author != null)
			return false;
	} else if (!author.equals(other.author))
		return false;
	if (description == null) {
		if (other.description != null)
			return false;
	} else if (!description.equals(other.description))
		return false;
	if (image == null) {
		if (other.image != null)
			return false;
	} else if (!image.equals(other.image))
		return false;
	if (title == null) {
		if (other.title != null)
			return false;
	} else if (!title.equals(other.title))
		return false;
	return true;
}

@Override
public int hashCode() {
	return Objects.hash(description, image, title, author);
}


}
