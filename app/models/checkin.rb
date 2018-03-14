class Checkin < ApplicationRecord
	belongs_to :room
  	belongs_to :student, optional: true
  	belongs_to :mentor, optional: true

  	validates :room_id, presence: true

  	def self.roster room
  		Checkin.where(room_id: room.id)
  	end

  	def self.students_at_hackathon
      if Student.count == 0
        return []
      end
  		check_in = Room.find_by_name("Hackathon Check-In")
      if check_in.nil?
        return []
      end
  		Checkin.where(room_id: check_in.id).where(is_mentor: false)
  	end

    def self.students_left_hackathon
      if Student.count == 0
        return []
      end
      check_out = Room.find_by_name("Hackathon Check-Out")
      if check_out.nil?
        return []
      end
      Checkin.where(room_id: check_out.id).where(is_mentor: false)
    end

    def self.mentors_at_hackathon
      if Mentor.count == 0
        return []
      end
      check_in = Room.find_by_name("Hackathon Check-In")
      if check_in.nil?
        return []
      end
      Checkin.where(room_id: check_in.id).where(is_mentor: true)
    end

    def self.mentors_left_hackathon
      if Mentor.count == 0
        return []
      end
      check_out = Room.find_by_name("Hackathon Check-Out")
      if check_out.nil?
        return []
      end
      Checkin.where(room_id: check_out.id).where(is_mentor: true)
    end
end
