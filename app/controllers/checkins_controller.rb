class CheckinsController < ApplicationController
  before_action :set_checkin, only: [:show, :edit, :update, :destroy]

  # GET /checkins
  # GET /checkins.json
  def index
    @checkins = Checkin.students
    @checkin_names = []
    @checkins.each do |checkin|
      @checkin_names << checkin.student.name
    end
  end
  # GET /checkins/1
  # GET /checkins/1.json
  def show
  end

  # GET /checkins/new
  def new
    @checkin = Checkin.new
  end

  # GET /checkins/1/edit
  def edit
  end

  def create_barcode_checkin
    upc = params[:upc][0...-1]
    room = Room.find_by_id(params[:room_id])

    params[:checkin] = Hash.new
    params[:checkin][:room_id] = room.id

    student = Student.find_by_upc(upc)
    if student.nil?
      mentor = Mentor.find_by_upc(upc)
      if mentor.nil?
        flash[:alert] = 'Unsuccessful Scan.'
        redirect_to :back
        return
      else
        if Checkin.where(:room_id => room.id, :mentor_id => mentor.id).length > 0
          flash[:alert] = 'Mentor already scanned into this room.'
          redirect_to :back
          return
        else
          params[:checkin][:mentor_id] = mentor.id
          params[:checkin][:is_mentor] = true
        end
      end
    else
      if Checkin.where(:room_id => room.id, :student_id => student.id).length > 0
        flash[:alert] = 'Student already scanned into this room.'
        redirect_to :back
        return
      else
        params[:checkin][:student_id] = student.id
        params[:checkin][:is_mentor] = false
      end
    end

    @checkin = Checkin.new(checkin_params)
    if @checkin.save
      flash[:notice] = 'Check In Successful.'
      redirect_to :back
    else
      flash[:alert] = 'Unsuccessful Scan.'
      redirect_to :back
    end
  end

  # POST /checkins
  # POST /checkins.json
  def create
    @checkin = Checkin.new(checkin_params)
    if @checkin.save
      flash[:notice] = 'Check In Successful.'
      redirect_to :back
    else
      flash[:alert] = 'Unsuccessful Check In.'
      redirect_to :back
    end
  end

  # PATCH/PUT /checkins/1
  # PATCH/PUT /checkins/1.json
  def update
  end

  # DELETE /checkins/1
  # DELETE /checkins/1.json
  def destroy
    @checkin.destroy
    redirect_to :back, notice: 'Check In was successfully destroyed.'
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_checkin
      @checkin = Checkin.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def checkin_params
      params.require(:checkin).permit(:is_mentor, :room_id, :student_id, :mentor_id)
    end
end
