class Checkin < ApplicationRecord
	belongs_to :room
  	belongs_to :student, optional: true
  	belongs_to :mentor, optional: true

  	validates :room_id, presence: true

  	def self.roster room
  		Checkin.where(room_id: room.id)
  	end

  	def self.at_hackathon
  		room = Room.find_by_name("Hackathon Check-In")
  		Checkin.where(room_id: room.id).where(is_mentor: false)
  	end
end
